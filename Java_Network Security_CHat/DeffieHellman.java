import java.math.BigInteger;
import java.util.Scanner;

 class DeffieHellman{


BigInteger n,g,x,y,A,B,ans;


			public String getPublicparam(BigInteger x){
				n = new BigInteger("1500450271");
				g = new BigInteger("13");
				ans = g.modPow(x,n);
				return (ans.toString());
			}
			public String getKey(BigInteger B,BigInteger x){
				n = new BigInteger("1500450271");
				g = new BigInteger("13");
				ans = B.modPow(x,n);
				return (ans.toString());
			}

}