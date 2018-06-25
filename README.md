# Java Network Security Chat Application


INTRODUCTION
---------------

The use of the project phase-2 is to understand explore the symmetric cipher availability and performance in JCA. The first part introduces the available algorithms for the Service Type "Cipher" using java.security.Security and java.security.provider classes.For that we have used Netbeans's built-in java standard edition. The environment used in the project is explicitly mentioned in the report provided. In the second part we have used AES and DES with Cipher Block Chaining to measure single-key encryption throughput and key testing rate. We used profiler to understand how CPU clock rate affect this and measured asked variables. The third part is about securely transfer messages with encryption from one client and send it to another client which decrypts the message via chat hub.

REQUIREMENTS
-------------

There is no special requirement for this phase. However, you need to use compatible version of JDK and JRE with provided Netbeans IDE. In addition, you could use outside file to ensure the length of string long enough to run at least 10 seconds(Part-2).

RECOMMENDED MODULES
--------------------

By using modular paradigm, we were able to make a class with unique methods which could be called up on instantiating the class with their instance. Some of the recommended modules to check are convertByteToString(), generateKeyTemp() etc.

CONFIGURATION
--------------   

There is no special configuration required other than setting up your Netbeans IDE on usual basis. However, you need to consider version of the IDE you are using. In case of Profiling, Eclipse IDE has external plugins which needs to be downloaded to profile efficiently. Netbeans provides built-in profiler.
