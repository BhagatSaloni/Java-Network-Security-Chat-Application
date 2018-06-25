//package chat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package project.phase2;
import java.io.IOException;
//import com.sun.xml.internal.messaging.saaj.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.Provider;
import java.security.Provider.Service;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 *
 * @author Abhishek
 */
public class ExploreJCA  {

    /**
     * @param args the command line arguments
     */
    
    static Map<String, List<Provider>> servicesMap = new HashMap<>();
    static Map<String, List<Provider>> algorithmsMap = new HashMap<>();
    static String type = "Cipher"; 
    
    ////////////////////////////////////////////////////////////////////////////
    //    Get details of Providers with services and algorithms provided by them. type is specific to "Cipher"
    //    so we can search which provider provides which algorithms.
    ////////////////////////////////////////////////////////////////////////////
    private static void getDetails(){
        
        System.out.println("Algorithm list is as under :\n");
        /*
        The loop provides information of providers and services which is stored in serviceMap.
            
        */
        for(Provider singleProvider_service: Security.getProviders()){
           
            for(Service singleService : singleProvider_service.getServices()){
                
               
                if(servicesMap.containsKey(singleService.getType())){
                    
                    final List<Provider> providerList_service = servicesMap.get(singleService.getType());
                    
                    if(!providerList_service.contains(singleProvider_service)){
                        
                        providerList_service.add(singleProvider_service);
                    }
                }
                else{
                        
                        final List<Provider> providers_service = new ArrayList<>();
                        providers_service.add(singleProvider_service);
                        servicesMap.put(singleService.getType(), providers_service);
                    }
                
            }
        }
        
             
        for(String serviceName : servicesMap.keySet()){
            
            System.out.println("Service Name : " + serviceName + " \nList of Provider(s) : " + Arrays.toString(servicesMap.get(serviceName).toArray()) + "\n");
        }
        
        
         /*
        The loop provides information of providers and algorithms provided by them which is stored in algorithmMap.
            
        */
        System.out.println("\n\n\n");
        
        for(Provider provider_algo : Security.getProviders()){
               
            for(Service singleServiceForAlgo : provider_algo.getServices()){
                    
                if(singleServiceForAlgo.getType().equals(type)){
                        
                    final String algo = singleServiceForAlgo.getAlgorithm();
                        
                    if(algorithmsMap.containsKey(algo)){
                            
                        final List<Provider> providerForAlgo = algorithmsMap.get(algo);
                            
                        if(!providerForAlgo.contains(provider_algo)){
                                
                            providerForAlgo.add(provider_algo);
                        }   
                    }    
                    else{
                                
                        List<Provider> providers = new ArrayList<>();
                        providers.add(provider_algo);
                        algorithmsMap.put(algo, providers);
                    }
                }
                    
            }
                
        }
       
        for(String algorithmName : algorithmsMap.keySet()){
            
            System.out.println("Algorithm Name : " + algorithmName + " \nList of Provider(s) : " + Arrays.toString(algorithmsMap.get(algorithmName).toArray()) + "\n");
        }
         
    }
    
    
    //Main Method
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        // TODO code application logic here
   
        
        //Method to get details of providers and Services
        //getDetails();
        
/*        JCATime obj = new JCATime();
        System.err.println("Processing...");
        
        //Method to encrypt message into chunks from a given string
        obj.singleKeyEncryption();
        
        //Testing a string using various keys to encrypt using AES
        obj.keyTestingRateAES();
        
        ////Testing a string using various keys to encrypt using DES
        obj.keyTestingRateDES();
        System.gc();
  */      
       // Test to check encryption and decryption of a String
        
        Conversation object = new Conversation();
        String test = "Abhi";
        String CipherText = object.encryptString(test);
        String PlainText = object.decryptString(CipherText);
        System.out.println("Entered Text:" + test + "\nCipher Text : " + CipherText + "\nPlain Text : " + PlainText);
        
    }
    
}
