import urllib.request
import urllib.parse
import hashlib
import hmac
import datetime

vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"

credentials = [
    ("8NDB7A68", "ZGHUPOZWXWCCXQAYADCAKRPQZFUHCVZY"),
    ("D1I9PZYX", "WNYFRJMWQYVKXIPAYZHTWJPMKBYJBYYY"),
    ("VNPAY987", "ZYZGTRXITKOTQQNMTUDBZLYYMYNXYEBC"),
    ("2TXCW237", "LYYAARLDRKMWPGLVDFZJUMZTYGIZVOKB"),
    ("CGXZLS0Z", "XNBCJFAKAZQSGTORHVNMSCGLLCGEYOOR"),
    ("F3QG7T5G", "XTVQJLZJFWPQYVFXDURYZTVZURVXKZWY"),
    ("7I11S67B", "TNCWUXCHDRNLYONKXGLAWRFXDHKCQKND"),
    ("1A6M2C46", "WNXCVYXCHWLZTYLBYZQXTUOHWKTQNWQX"),
    ("9TIV0HBB", "EILMXVTVWFRHYTTVUAKNDOSMBLRNYEIK"),
    ("Z0P077B8", "UNKNOWN"),
    ("NVM9441Z", "MUMFYZLYHOCYYEIBIBKCQXWJFWQYYGRI")
]

def test_credential(tmn_code, secret_key):
    vnp_Version = "2.1.0"
    vnp_Command = "pay"
    amount = "10000000"
    vnp_IpAddr = "127.0.0.1"
    vnp_TxnRef = "TEST123456"
    vnp_OrderInfo = "Thanh toan"
    vnp_OrderType = "other"
    vnp_ReturnUrl = "http://localhost:8080"
    
    # Time
    now = datetime.datetime.now()
    vnp_CreateDate = now.strftime('%Y%m%d%H%M%S')
    
    vnp_Params = {
        "vnp_Version": vnp_Version,
        "vnp_Command": vnp_Command,
        "vnp_TmnCode": tmn_code,
        "vnp_Amount": amount,
        "vnp_CurrCode": "VND",
        "vnp_TxnRef": vnp_TxnRef,
        "vnp_OrderInfo": vnp_OrderInfo,
        "vnp_OrderType": vnp_OrderType,
        "vnp_ReturnUrl": vnp_ReturnUrl,
        "vnp_IpAddr": vnp_IpAddr,
        "vnp_CreateDate": vnp_CreateDate,
        "vnp_Locale": "vn"
    }

    # Sort
    keys = sorted(vnp_Params.keys())
    hashData = []
    query = []
    for key in keys:
        val = vnp_Params[key]
        hashData.append(f"{key}={urllib.parse.quote_plus(val)}")
        query.append(f"{urllib.parse.quote_plus(key)}={urllib.parse.quote_plus(val)}")
        
    hashDataStr = "&".join(hashData)
    queryStr = "&".join(query)
    
    # hash
    vnp_SecureHash = hmac.new(secret_key.encode('utf-8'), hashDataStr.encode('utf-8'), hashlib.sha512).hexdigest()
    queryStr += "&vnp_SecureHash=" + vnp_SecureHash
    
    full_url = f"{vnp_PayUrl}?{queryStr}"
    
    # Make request
    req = urllib.request.Request(full_url, headers={'User-Agent': 'Mozilla/5.0'})
    try:
        html = urllib.request.urlopen(req).read().decode('utf-8')
        if "Thanh toan don hang" in html or "Payment Gateway" in html or "So the" in html or "Thẻ ATM" in html:
            return "SUCCESS"
        elif "Error" in full_url:
            return "ERROR"
        return "UNKNOWN_HTML"
    except urllib.error.HTTPError as e:
        if e.code in [302, 301]:
            # Redirect usually to error or success
            loc = e.headers.get('Location')
            if 'Error.html' in loc:
                return f"ERROR_REDIRECT ({loc})"
            return f"REDIRECT ({loc})"
        else:
            try:
                content = e.read().decode('utf-8')
                if "Error" in content or "tồn tại" in content or "Sai chữ ký" in content:
                    return f"ERROR_PAGE"
                return f"HTTP_{e.code}"
            except:
                return f"HTTP_{e.code}"
    except Exception as e:
        return str(e)


found_success = False
for tmn, secret in credentials:
    res = test_credential(tmn, secret)
    print(f"Testing {tmn} / {secret[:5]}... -> {res}")
    if res == "SUCCESS":
        print("^^^ THIS ONE WORKS!")
        found_success = True

if not found_success:
    print("NO WORKING CREDENTIALS FOUND.")
