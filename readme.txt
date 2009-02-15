
Gmanda - Gmane Mailing List Qualtitative Data Analysis Tool
-----------------------------------------------------------

Gmanda was written to support the qualitative analysis of large mailing-lists
downloaded from the Gmane mailing-list service.

Gmanda's unit of coding is a single message.

---+ Running

The main class to call to run Gmanda is 

de.fu_berlin.inf.gmanda.GmandaMain

You need most probably more memory (for instance '-Xmx512m') 

---+ Developing

Please stick to the following rules and code conventions when developing at GmanDA.

   * All code and comments should be in English language.
  
   * Visibility policy:
      * By default all attributes and methods should be protected or public.
      * If you choose to restrict visibility further (package or private) 
        you need to explain the reason for this in the JavaDoc comment. 
         * Typical reasons can be:
            * An attribute is computed by a method and its value is dependent 
              on other values.
            * An attribute is used by a method for optimization and caching and its 
              current value is hard to describe to an external user.
         * The following are NO valid reasons:      
            * You are embarrased at your own code quality and do not want others to
              use your method.
            * The method is not part of the public interface of the class, but rather
              performs internal computation (to achieve this, you should use an interface)
    
   * Rules for Variable Names
      * Variable names should be a good compromise between being self-explaining 
        and being short.
      * Common local variables such as primitive variables for loop iteration or condition testing may be named with a short name that matches their class name:
          * int i;
          * String s;
          * File f;
          * float f;
          * double d;
          * Iterator<...> it;
      * All other variables should have full names that help
        understanding their use. In many cases the the class
        name with an initial lower-case letter is okay:
          * ISessionListener => sessionListener
      * Use camelCaseInVariableNames. The first letter is lower-case!
       
   * Control flow guidelines:
      * Test whether you can return from a method instead of testing whether you
        should execute a block of code.
        <verbatim>
	    Instead of 
	    
	    public void ... {  
		    ...
		    if (condition){
		        // long block of code
		    }
	    }
       </verbatim>
	    
	    you should write
	    
       <verbatim>
	    public void ... {
	    
	        ...
	        if (!condition)
	            return
	            
	        // long block of code
	    }
       </verbatim>	    

   * Methods may assume that they are called with correct non-null input unless the method specifies that it allows incorrect or null input.
      * If a parameter may be null or is checked add a @param-JavaDoc that indicates this.

  * Before committing:
    * Write a changelog entry into changelog.txt
    * Fix all Eclipse warnings
    * Format the source code and organize imports
    
  

 
