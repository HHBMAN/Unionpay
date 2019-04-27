package com.unionpay.acp.demo;

import com.unionpay.acp.sdk.CertUtil;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 对控件给商户APP返回的应答信息验签，前段请直接把string型的json串post上来
 */
public class VerifyAppData extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init();
		SDKConfig.getConfig().loadPropertiesFromSrc();
		CertUtil.getSignCertId(); //这句别删，要触发CertUtil加载BC，目前就这么凑合着用吧……

		//测试环境公钥（环境有问题临时使用）
		String modulus = "24632934472548302293036226089824968421661361645637431123880630200634091646128480357421278291389131168685456684602745983960572325709608985776102347569806268936575478092784449852549338979183538940554247390154750040744752638737999273664044187258798780589002130604456892447336326452896357640847880796483787691907988056144795507570666940572380888876930347241360292714982540717113137889565061472409075768421610232488996955334246858017193096212668296441440675907340110513285462916481537628039324808323948113551801025627191978546952179958121647927353796708777028511074901407765357016353537612417688290507646465109621974577877";
	    String publicExponent = "65537";

		//测试环境公钥（环境没问题正常应该是这个）
//		String modulus = "24870613246304283289263670822577417714537477136695312218046086562441084140352408862449003198972758030370375896331356438381534807815999481415930217971513079824183591552429779125222230389655838097565141139205829591128287005548898062000970767426912014994392229218979869216370190349843903870279325956661459861716847460988265260792970759967490015941772320263508330685602563839220027394572548955687677315821727057921756004005781874479358265172016335126486731385109336772938263090077762887508722625235251295041241798219236919770312254416281253815794530657627243362881204125234159183339122880098511453026644263131341899862471";
//	    String publicExponent = "65537";
	    
	    //生产环境公钥
//	    String modulus = "24882698307025187401768229621661046262584590315978248721358993520593720674589904440569546585666019820242051570504151753011145804842286060932917913063481673780509705461614953345565639235206110825500286080970112119864280897521494849627888301696007067301658192870705725665343356870712277918685009799388229000694331337917299248049043161583425309743997726880393752539043378681782404204317246630750179082094887254614603968643698185220012572776981256942180397391050384441191238689965500817914744059136226832836964600497185974686263216711646940573711995536080829974535604890076661028920284600607547181058581575296480113060083";
//	    String publicExponent = "65537";
	        
		appVerifyPubKey = getPublicKey(modulus, publicExponent);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//struts如果读不到输入的话,前段试试修改post时的content-type为text/html
		String jsonData = new String (IOUtils.toByteArray(req.getInputStream()), DemoBase.encoding);
		//resp.getWriter().write(jsonData);
		LogUtil.writeLog("控件应答信息验签处理开始：[" + jsonData + "]");
		boolean result = this.validateAppResponse(jsonData, DemoBase.encoding);
		resp.getWriter().write(result?"true":"false");
		LogUtil.writeLog("控件应答信息验签" + (result?"成功":"失败"));
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	private PublicKey appVerifyPubKey = null;
	
    /**
     * 从模数指数取公钥，建议改public static挪到个util类里。
     * @param modulus
     * @param exponent
     * @return
     */
	private PublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			LogUtil.writeErrorLog("公钥获取失败。");
			return null;
		}
	}

	/**
	 * 对控件支付成功返回的结果信息中data域进行验签（控件端获取的应答信息）<br>
	 * （目前环境有问题的样例）{  "sign" : "mtArQuB7XVMV/+CVqviSh7ntml5nbXpr8SDDiOQ+UjLPrAxzCE7r5huk63XCxej1ZMWNOIUdb0BLMovPlxZ6sS2Cifi8opF6Kcz8bhdi9Km7J69bLJNIRwRvNYM6SjHzn59hL6ZPiubas7/6nmz1QnESe7bdJAD5wCg6mB9oHdTncy83AWq8zvbYYIxGIzi5H0gHIsZbUdvMhljcxcnWLfdAdp5/BVEkSS7rdbUUOF6wu3RVrrKpI1C50Dcdx1yWGX1vOdrEJ6HWiZRpxb8ZtRE0/kCQFLaFHT9f8Hv4MLGOHB5tHPK2kWQIqidHJcXr3EPfH9Pu3YZ0MRH3vOVRkw==",  "data" : "pay_result=fail&tn=710065895678926661300&cert_id=69026276696"}
     * （正常的测试环境样例）{"sign" : "J6rPLClQ64szrdXCOtV1ccOMzUmpiOKllp9cseBuRqJ71pBKPPkZ1FallzW18gyP7CvKh1RxfNNJ66AyXNMFJi1OSOsteAAFjF5GZp0Xsfm3LeHaN3j/N7p86k3B1GrSPvSnSw1LqnYuIBmebBkC1OD0Qi7qaYUJosyA1E8Ld8oGRZT5RR2gLGBoiAVraDiz9sci5zwQcLtmfpT5KFk/eTy4+W9SsC0M/2sVj43R9ePENlEvF8UpmZBqakyg5FO8+JMBz3kZ4fwnutI5pWPdYIWdVrloBpOa+N4pzhVRKD4eWJ0CoiD+joMS7+C0aPIEymYFLBNYQCjM0KV7N726LA==",  "data" : "pay_result=success&tn=201602141008032671528&cert_id=68759585097"}
     * @return 是否成功
	 */
	private boolean validateAppResponse(String jsonData, String encoding) {
		
		if (SDKUtil.isEmpty(encoding)) {
			encoding = "UTF-8";
		}

        Pattern p = Pattern.compile("\\s*\"sign\"\\s*:\\s*\"([^\"]*)\"\\s*");
		Matcher m = p.matcher(jsonData);
		if(!m.find()) {
			LogUtil.writeErrorLog("内容不正确。");
			return false;
		}
		String sign = m.group(1);

		p = Pattern.compile("\\s*\"data\"\\s*:\\s*\"([^\"]*)\"\\s*");
		m = p.matcher(jsonData);
		if(!m.find()) {
			LogUtil.writeErrorLog("内容不正确。");
			return false;
		}
		String data = m.group(1);

		try {
			MessageDigest md = null;
			md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(data.getBytes(encoding));
			byte[] bs = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : bs) { 
			     String hex = Integer.toHexString(b & 0xFF); 
			     if (hex.length() == 1) { 
			       hex = '0' + hex; 
			     }
			     sb.append(hex);
			}
			Signature st = Signature.getInstance("SHA1withRSA", "BC");
			st.initVerify(this.appVerifyPubKey);
			st.update(sb.toString().toLowerCase().getBytes(encoding));
			return st.verify(Base64.decodeBase64(sign));
		} catch (Exception e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
		} 
		return false;
	}
}