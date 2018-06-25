import java.security.*;
import java.security.spec.*;
import javax.crypto.KeyAgreement;

/**
 *
 * @author yagnikbhatt
 */
public class ECDH {

     public KeyPair getKeyPair() throws Exception{
			KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
			ECGenParameterSpec ecspec;
			ecspec = new ECGenParameterSpec("secp256r1");
			generator.initialize(ecspec);
			KeyPair keypair = generator.genKeyPair();
			return keypair;
    }
     
      public byte[] generateSharedKey(PrivateKey privKey,PublicKey pubKey) throws Exception {
		    KeyAgreement agreement=KeyAgreement.getInstance("ECDH");
		    agreement.init(privKey);
		    agreement.doPhase(pubKey,true);
		    byte[] sharedSecretKey = agreement.generateSecret();
		   return sharedSecretKey;
	
		}
     
     
}
