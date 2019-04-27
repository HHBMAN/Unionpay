<?php
header ( 'Content-type:text/html;charset=utf-8' );
include_once $_SERVER ['DOCUMENT_ROOT'] . '/upacp_demo_app/sdk/acp_service.php';

/**
 * 对控件给商户APP返回的应答信息验签，前段请直接把string型的json串post上来
 */
$data = file_get_contents('php://input', 'r');
echo validateAppResponse($data) ? "true" : "false";

/**
 * 对控件支付成功返回的结果信息中data域进行验签
 * @param $jsonData json格式数据，例如：
 * （目前环境有问题的样例）{  "sign" : "mtArQuB7XVMV/+CVqviSh7ntml5nbXpr8SDDiOQ+UjLPrAxzCE7r5huk63XCxej1ZMWNOIUdb0BLMovPlxZ6sS2Cifi8opF6Kcz8bhdi9Km7J69bLJNIRwRvNYM6SjHzn59hL6ZPiubas7/6nmz1QnESe7bdJAD5wCg6mB9oHdTncy83AWq8zvbYYIxGIzi5H0gHIsZbUdvMhljcxcnWLfdAdp5/BVEkSS7rdbUUOF6wu3RVrrKpI1C50Dcdx1yWGX1vOdrEJ6HWiZRpxb8ZtRE0/kCQFLaFHT9f8Hv4MLGOHB5tHPK2kWQIqidHJcXr3EPfH9Pu3YZ0MRH3vOVRkw==",  "data" : "pay_result=fail&tn=710065895678926661300&cert_id=69026276696"}
 * （正常的测试环境样例）{"sign" : "J6rPLClQ64szrdXCOtV1ccOMzUmpiOKllp9cseBuRqJ71pBKPPkZ1FallzW18gyP7CvKh1RxfNNJ66AyXNMFJi1OSOsteAAFjF5GZp0Xsfm3LeHaN3j/N7p86k3B1GrSPvSnSw1LqnYuIBmebBkC1OD0Qi7qaYUJosyA1E8Ld8oGRZT5RR2gLGBoiAVraDiz9sci5zwQcLtmfpT5KFk/eTy4+W9SsC0M/2sVj43R9ePENlEvF8UpmZBqakyg5FO8+JMBz3kZ4fwnutI5pWPdYIWdVrloBpOa+N4pzhVRKD4eWJ0CoiD+joMS7+C0aPIEymYFLBNYQCjM0KV7N726LA==",  "data" : "pay_result=success&tn=201602141008032671528&cert_id=68759585097"}
 * @return 是否成功
 */
function validateAppResponse($jsonData) {
	
	//测试环境公钥（环境有问题临时使用）
	$key_str = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwyFjugBEq4Pt+B4G1PZk\nqYHn9LJemr69XWw/yI42WDOt0qEItwmR2VMqMswSqHUc6MCa8F5nfoQpnXCH2r3b\n38IusfqbSTafXRLaXxAJyZPuKrq4kBY9XTYr+rj/AEYJ9O1DIYHWAew/GgNY++2J\nNUTse/ixXqO62Q3XXtlNaKT8YyKjj6oj56O/CTF6RZ+y4hj17u+W8GNq2CBhBCJp\n8qYImLltb6PVDhuOzTFk57CHcuvBarj0sORSqeX6C/NuRjV96CUmbovHdlA5DCwW\nrH9Oic9CSs+QFW5WiPrNuP4gn6PK/ulmgrPHugQ5GooyZEf270QkHkKW191fFqEy\n1QIDAQAB\n-----END PUBLIC KEY-----";

	//测试环境公钥（环境没问题正常应该是这个）
// 	$key_str = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxQNhgIti+/dz3XXOFEOt\nJYJ50BgMlB58lSNkojZq8W0Hb9ttKYpB3Q1W+H7Ne16EJgvEo6W07oMNNEfAf3F+\nFFjcIEyZw693bFBC8wlxX1fCiq0IqqHOC6gPEl9h4a6K3sOm5GwMYjT7IqBzVFjb\ny/VW25cbzjl9M/s9G0mtqt5BiVBeCEyAcUBn+9F2qUulWg2ZrkLnpwmTlt+vaeAO\nVdtSo9cuLWOJcAVv/Lhvx4YQiXUuYqndo8wM8rt6yFzt8s40SnWhDkl3WcT8ZmP6\nULfjlo1aYuyX/T24C+jgu76lCyYbJ8ttQZHnUpuYdqMXgyuvq5KqJTbUK2vW5AaR\nxwIDAQAB\n-----END PUBLIC KEY-----";

	//生产环境公钥
// 	$key_str = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxRvjZwVzfvmlFAG4FRTr\nf6YtjHAe/T/rZpEvfD55Y/qRLwnNp5batlWc3ybxm9VBOpVyl6AhvjaLGTI+uLnW\nvQ7trqe/GfdhxnU9rZGdEWqKLkeIwmNAZs9Puczm40etO78AU75Z/YnRvWRiti5t\n+UEsY6S6JUTxJ8/64MRRvXs8VXyPXkOyZrgZPzl8s4wNSUMADIXQE9xiTtCTJjA7\nwkNKTck7LtaMkDyaAmld6j36XL2rTCmLFcaNmXqIKsNsdi+ihPATg64+pulHtWlD\nJ/QodV20f3QfDNjPp1bTgFW+gwOpFbjRZTJdrs8Q/DEJYaLPGk/rwsdb/adtZN/o\n8wIDAQAB\n-----END PUBLIC KEY-----";
	
	$data = json_decode($jsonData);
	$sign = $data->sign;
	$data = $data->data;
    $public_key = openssl_pkey_get_public ($key_str);
	$signature = base64_decode ( $sign );
	$params_sha1x16 = sha1 ( $data, FALSE );
	$isSuccess = openssl_verify ( $params_sha1x16, $signature,$public_key, OPENSSL_ALGO_SHA1 );
	return $isSuccess;
}
