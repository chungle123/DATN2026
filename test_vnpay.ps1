$vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"

$credentials = @(
    @("D1I9PZYX", "WNYFRJMWQYVKXIPAYZHTWJPMKBYJBYYY"),
    @("VNPAY987", "ZYZGTRXITKOTQQNMTUDBZLYYMYNXYEBC"),
    @("8NDB7A68", "ZGHUPOZWXWCCXQAYADCAKRPQZFUHCVZY"),
    @("2TXCW237", "LYYAARLDRKMWPGLVDFZJUMZTYGIZVOKB"),
    @("J49WB8I2", "ONNTPWUNJYZMXXWBLXLLAMUONZOKFIVF"),
    @("F3QG7T5G", "XTVQJLZJFWPQYVFXDURYZTVZURVXKZWY"),
    @("7D9L90V7", "SOPQLXIFEXDDBJIVMBLUKUKVOSCQYUKN"),
    @("GXOPM8YJ", "IPQGEYOGDUSLYKOPZBYKYCCTRYMQEXKL"),
    @("QPM8G227", "PFFJAYXFYZOSBHRJOKHIKBEXXWTVHQQP"),
    @("NVM9441Z", "MUMFYZLYHOCYYEIBIBKCQXWJFWQYYGRI")
)

function Test-VNPayCredential {
    param([string]$TmnCode, [string]$SecretKey)
    
    $vnp_Version = "2.1.0"
    $vnp_Command = "pay"
    $amount = "10000000"
    $vnp_IpAddr = "127.0.0.1"
    $vnp_TxnRef = "TEST123456"
    $vnp_OrderInfo = "Thanh toan"
    $vnp_OrderType = "other"
    $vnp_ReturnUrl = "http://localhost:8080"
    $vnp_CreateDate = (Get-Date).ToString("yyyyMMddHHmmss")
    
    $vnp_Params = [ordered]@{
        "vnp_Amount" = $amount
        "vnp_Command" = $vnp_Command
        "vnp_CreateDate" = $vnp_CreateDate
        "vnp_CurrCode" = "VND"
        "vnp_IpAddr" = $vnp_IpAddr
        "vnp_Locale" = "vn"
        "vnp_OrderInfo" = $vnp_OrderInfo
        "vnp_OrderType" = $vnp_OrderType
        "vnp_ReturnUrl" = $vnp_ReturnUrl
        "vnp_TmnCode" = $TmnCode
        "vnp_TxnRef" = $vnp_TxnRef
        "vnp_Version" = $vnp_Version
    }

    $hashData = @()
    $query = @()
    
    foreach ($key in $vnp_Params.Keys | Sort-Object) {
        $val = $vnp_Params[$key]
        $encodedVal = [uri]::EscapeDataString($val)
        $encodedKey = [uri]::EscapeDataString($key)
        $hashData += "$($key)=$encodedVal"
        $query += "$($encodedKey)=$encodedVal"
    }

    $hashDataStr = $hashData -join "&"
    $queryStr = $query -join "&"

    $hmac = New-Object System.Security.Cryptography.HMACSHA512
    $hmac.Key = [Text.Encoding]::UTF8.GetBytes($SecretKey)
    $hashBytes = $hmac.ComputeHash([Text.Encoding]::UTF8.GetBytes($hashDataStr))
    
    $vnp_SecureHash = ($hashBytes | ForEach-Object { $_.ToString("X2") }) -join ""
    $vnp_SecureHash = $vnp_SecureHash.ToLower()

    $fullUrl = "$vnp_PayUrl`?$queryStr&vnp_SecureHash=$vnp_SecureHash"
    
    try {
        $response = Invoke-WebRequest -Uri $fullUrl -UseBasicParsing -MaximumRedirection 0 -ErrorAction Stop
        if ($response.Content -match "Error.html") {
            return "ERROR_PAGE"
        }
        return "SUCCESS?"
    } catch {
        if ($_.Exception.Response.StatusCode -eq 302 -or $_.Exception.Response.StatusCode -eq 301) {
            $location = $_.Exception.Response.Headers.Location
            if ($location -match "Error.html") {
                return "ERROR ($location)"
            }
            return "SUCCESS_REDIRECT"
        }
        $errContent = ""
        if ($_.Exception.Response) {
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $errContent = $reader.ReadToEnd()
        }
        if ($errContent -match "Thanh toan" -or $errContent -match "Payment Gateway") {
            return "SUCCESS_PAGE"
        }
        if ($errContent -match "Error" -or $errContent -match "tồn tại" -or $errContent -match "Sai chữ ký") {
            return "ERROR_IN_BODY"
        }
        return "HTTP ERROR: $($_.Exception.Message)"
    }
}

foreach ($cred in $credentials) {
    Write-Host ("Testing {0}..." -f $cred[0])
    $res = Test-VNPayCredential -TmnCode $cred[0] -SecretKey $cred[1]
    Write-Host "`t-> $res"
}
