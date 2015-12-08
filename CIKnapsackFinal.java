 	package BE;
	import java.lang.*;
	import java.io.*;
	import java.util.Scanner;
	import java.util.Random;
						
	class Objects
	{
		String name;
		float weight,value;
		float pSum;
		int useCount;
		int chosenCount;
		public Objects(String name,float weight,float value)
		{
			this.name=name;
			this.weight=weight;
			this.value=value;
			useCount=0;
			chosenCount=0;
		}
	};

	class Candidate
	{
		
		int name;
		int cSize;
		Objects[] co;
		float fv,fw;
		float p,pw,pv,pd;
		int objectCount;
		Candidate followee;	
		static float W;
		
		boolean feasible;
		int fsw;
		public Candidate(int name,int cSize)
		{
			this.name=name;
			this.cSize=cSize;
			co=new Objects[cSize];
			fv=0;
			fw=0;   
			p=0;
			pv=0;
			pw=0;
			feasible=false;
			fsw=0;
			objectCount=0;
			
			for(int i=0;i<cSize;i++)
			{
				co[i]=null;
			}
		}
					
		public void addObject(Objects o)
		{  //add object wherever there is vacancy
			boolean added =false;
			int c=0;
			while(added==false && c<cSize)
			{
			 if(co[c]==null)
			 {
			  co[c]=o;
			  added=true;
			  objectCount++;
			 }
			 else
			 {
				 c++;
			 }
			}
		}//end of addObject
    //    public void ifFull()
		public void removeObject(int l)
		{
            co[l]=null;//remove candidate object at index l
			objectCount--;
			for(int i=0;i<cSize;i++)
			{
				if(co[i]==null)
				{
					int k=i+1;
					while(co[i]==null && k<cSize)
					{
						if(co[k]!=null)
						{
                         co[i]=co[k];
						 co[k]=null;
						}
						else
						{ 
							k++;
						}
					}//eo while
				}
			}//eo for

		}
		public float calculateFw()
		{
			int i=0;
			fw=0;
			for(i=0;i<cSize;i++)
			{  
			  if(co[i]!=null)
			  {
			   fw=fw+co[i].weight;
			   
			  }		
			 }//end of for
			
			 if(fw<=Candidate.W)
			 {
				 feasible=true;
				 fsw=1;
				// System.out.println("Hi "+Candidate.W);
			 }
			 else
			 {
				 feasible=false;
				 fsw=0;
				 //System.out.println("Hello "+Candidate.W);
			 }
			 this.fw=fw;
		 return fw;
		}//end of fwSum
	   
	   public float calculateFv()
	   {
		   int i=0;
		   fv=0;

		   for(i=0;i<cSize;i++)
		   {
			   if(co[i]!=null)
			   {
				 fv=fv+co[i].value;
			   }
		   }
		   this.fv=fv;
		   return fv;
	   }//end of fvSum

	   
	   public float calculateP(float fvSum,float W)
	   {
		 float k;
		 pv=fv/fvSum;
		 //System.out.println("pv = "+pv);
		 k=2;
		 if(fw<=W)
		 {
		   pw=fw/W;
		 }
		 else if(fw>W)
		 {
		  pw=3-(k*fw/W);
		 }
		 //System.out.println("pw = "+pw);
		 p=pw+pv;
		 return p;
	   }
      
	  //method to check if particular object exists in the candidate
	  public boolean ifExists(String name)
	  {
         int i=0;
		 boolean exists=false;	
			for(i=0;i<cSize;i++)
			{  
			  if(co[i]!=null)
			  {
		       if(co[i].name.equals(name))
			   {
			     exists=true; 			   
			   }
			  }
			 }//end of for
			 return exists;
	  }
	  
	 };//end of class Candidate

	 

	class Cohort
	{	
		Candidate [] candidate;
		int size;
		static int N;
		float fvSum,fwSum,po;
		float [] pArray;
		int t;
		public Cohort(int size,float po)
		{
			this.size=size;
			this.po=po;
			fvSum=0;
			fwSum=0;
			candidate= new Candidate[size];
			pArray=new float[size];
		}
		
	};

	public class CIKnapsackFinal
	{
		 public static void main(String[] args)
		 {
		
		   Objects[] o;
		   Cohort cohort;
		   Scanner in;
		   Candidate[] variation;
		   int N;
		   float W;
		   int i,j,t;
		   in=new Scanner(System.in);
		   float wSum=0;
		   int cSize;
		   
		  //Accept no if items and  weight capacity
		  
		  System.out.println("Enter no of items and Knapsack Capacity");
		  System.out.println("N = ");
		  N=in.nextInt();
		  Cohort.N=N;
		  System.out.println("W = ");
		  W=in.nextFloat();
		  Candidate.W=W;
		 
		  o=new Objects[N];
		  //Accept the objects and their weights and values


		  String name;
		  float weight,value;
		  System.out.println("Enter the name,weights,values of following items");
		  for(i=0;i<N;i++)
		  { 
			System.out.println("Object "+i);
			name=Integer.toString(i+1);
			System.out.println("Name = "+name);
			
			System.out.println("Weight =");
			weight=in.nextFloat();
			System.out.println("Value =");
			value=in.nextFloat();
	        wSum=wSum+weight;
			o[i]=new Objects(name,weight,value);
		  
		  }

		  //display objects
		  System.out.println("\n\nObject    Weight   Value");

		  for(i=0;i<N;i++)
		  {
			System.out.println(" "+o[i].name+"       "+o[i].weight+"     "+o[i].value);

		  }
		  System.out.println("\nwSum= "+wSum);
          
		  //Feasible Solution Generator
             
		  //Calculate no. of Candidates from no. of object and percentage repetition

		
		  int[] heaviest=new int[N];
		  for(i=0;i<N;i++)
		  {
			  heaviest[i]=i;
		  }
		  int templ;
 		  for(i=0;i<N;i++)
		  {
			  for(j=i+1;j<N;j++)
			  {
				  if(o[heaviest[j]].weight>o[heaviest[i]].weight)
				  {
					templ=heaviest[i];
					heaviest[i]=heaviest[j];
					heaviest[j]=templ;
				  }
			  }
		  }
         System.out.println("Heaviest weight is "+o[heaviest[0]].weight);
		 float minPo=(o[heaviest[0]].weight/W)*100;
		 float maxPr=100-minPo;
		
		
		 //Begining of CI Program 
		  int forW=0;

        String ans;
	   do{
		  if(forW!=0)
		  {
		   System.out.println("Knapsack Capacity W = ");
		   W=in.nextFloat();
		   Candidate.W=W;
		  }
		  forW=1;

		
        System.out.println("Min po ="+minPo);
		 System.out.println("Max pr ="+maxPr);

		 System.out.println("\nEnter the percentage repetition of objects in Candidates(0-100)");
		  float pr=in.nextFloat();
		  float po=100-pr;
          float avgWt=wSum/N;
		  //Here goes the calculation for candidate no generation
		 float interimOpc=(float)Math.round((W/avgWt)+1);
		 System.out.println("\nInterim oPc = "+interimOpc);
		  float objPerCandidate=interimOpc;
          System.out.println("\nObjects/Candidate = "+objPerCandidate);
		      //Preliminary no. of Candidates
          float pNc=N/objPerCandidate;
          System.out.println("\nPreliminary no. of Candidates = "+pNc);

		  //Final no. of Candidates
		  float fNc=pNc*100/po;
		  System.out.println("\nFinal no. of Candidates = "+fNc);
		 int cohortSize=Math.round(fNc); 
         
		 cohort=new Cohort(cohortSize,po);
         
           //population of candidates with

		  //each candidate will be asked to populate itself by adding objects randomly
		  // This process will happen in a way that all objects have been occupied
		  // by atleast one if not more candidates
		  cSize=Math.round(objPerCandidate*2);

		  for(i=0;i<cohortSize;i++)
		  {
			cohort.candidate[i]=new Candidate(i,cSize);         
		  }     
		  populateCandidate(cohort,o);
		  //System.out.println("\nCame out of Populate Candidate method");  



		   //now calculate fv and fw of each candidate and 
		  //and as we do that, calculate fvSum and fwSum of the Cohort
	 
		  for(i=0;i<cohortSize;i++)
		  {
			  //fwSum
				cohort.fwSum=cohort.fwSum+cohort.candidate[i].calculateFw();
			  //fvSum
				cohort.fvSum=cohort.fvSum+cohort.candidate[i].calculateFv();
		  }	
		  
		
		
		//Display Candidates 

		  System.out.println("\nCandidates              Name   Weight   Value   Feasibility  Object Count"); 
		  for(i=0;i<cohortSize;i++)
		  { 
              System.out.println("\n\nCandidate "+i+" :");
			  System.out.print("                                                 "+cohort.candidate[i].feasible);
			  System.out.print("       "+cohort.candidate[i].objectCount);
			for(j=0;j<cohort.candidate[i].cSize;j++)
			{
			  if(cohort.candidate[i].co[j]!=null)
			  {
				  System.out.print("\nCandidate Object "+j+" :");
				  System.out.print("       "+cohort.candidate[i].co[j].name);
				  System.out.print("     "+cohort.candidate[i].co[j].weight);
				  System.out.print("     "+cohort.candidate[i].co[j].value);
				    
			  }
			  /*else
			 {
				  System.out.println("\n null");
			 }
			 */

			}//end of for j
			  
		}//end of for i
		  
		  
		  System.out.println("\n\nCohort's fwSum ="+cohort.fwSum);
		  System.out.println("Cohort's fvSum= "+cohort.fvSum);


		//  System.out.println("Enter no. of Candidates in Cohort");
		// int cohortSize=in.nextInt();
		  System.out.println("\n\nEnter the no of variations in CI calculations");
		  cohort.t=in.nextInt();
		  t=cohort.t;
		   
		  
		  
		  //to find best and worst candidates ,required variables and structures

		  Candidate bestCandidate,worstCandidate,pbestCandidate,pworstCandidate;
          int rankArray[]=new int[cohort.size];
		  int tem;
		  float pbfv,pwfv;

		  // finding the best and worst candidate
          for(i=0;i<cohort.size;i++)
		  {
			  rankArray[i]=i;
		  }
		  //logic for maintaing rank
		 
		   
		  for(i=0;i<cohort.size;i++)
		  {
			  for(j=i+1;j<cohort.size;j++)
			  {
				  if(cohort.candidate[j].fv>cohort.candidate[i].fv)
				  {
                    tem=rankArray[i];
					rankArray[i]=rankArray[j];
					rankArray[j]=tem;
				  }
			  }
		  }
          
		  bestCandidate=cohort.candidate[rankArray[0]];
		  worstCandidate=cohort.candidate[rankArray[cohort.size-1]];
		  System.out.println("\n\nBest Candidate = "+bestCandidate.name);
          System.out.println("Worst Candidate = "+worstCandidate.name);
		  pbfv=bestCandidate.fv;
		  pwfv=worstCandidate.fv;

		   // t,variation,Candidate,cohort
		  variation=new Candidate[t+1];
          
          //create objects of variation array
		  for(i=0;i<=t;i++)
		  {
			  variation[i]=new Candidate(i,cSize);
		  }


      boolean saturation=false;
	  Random rand=new Random();
		  int rChoice,oaChoice,orChoice;
		  boolean added=false;
		  int attempt=0;
		  int choiceMemory[]=new int[40];
		  
		  int f;
		  int d;
		  int k;
		  
 //BEGINS COHORT Learning Loop

      while(saturation==false)
	  {
		    // Calculate p(probability distribution) for each candidate and display it
		  
		  System.out.println("\nProbability Distribution for each Candidate");
		 
		  probabilityDistribution(cohort,W);
		  
		  //Each candidate selects his followee randomly
		  
		  System.out.println("\n Followees of each candidate");

		  for(d=0;d<9;d++)
		  {
		   for(int g=0;g<cohortSize;g++)
		   {
			
			f=findFollowee(cohort.pArray,cohortSize);
			if(f!=g)
			{
		//	System.out.print("\nCandidate "+g+" follows candidate ");
			cohort.candidate[g].followee=cohort.candidate[f];
			System.out.print(f);
			}
		   }
		  }//end of for(d)
		  
       
		  //COHORT LEARNING
         
        


		    for(int c=0;c<cohort.size;c++)//for each candidate
		    {
             //code for making each candidate learn from followee

			 //1. Make copies of the candidate in variation array of candidates

             Candidate follower=cohort.candidate[c];
			 Candidate followee=cohort.candidate[c].followee;
			 System.out.println("\n\nCandidate : "+follower.name+" follows "+followee.name);

             for(k=0;k<=t;k++)//for each variation
			 {
                 for(int p=0;p<variation[k].cSize;p++)//for each object in follower to be added to variation
			   	 {
					 variation[k].addObject(follower.co[p]);
				 }
				 variation[k].fv=follower.fv;
				 variation[k].fw=follower.fw;
				 variation[k].p=follower.p;
				 variation[k].objectCount=follower.objectCount;
				 variation[k].fsw=follower.fsw;
				 variation[k].feasible=follower.feasible;
			 }//end of for k

		/*
			        //Display Variations 

		  System.out.println("\nVariation                  Name       Weight      Value     fw    fv	 fsw"); 
		  for(i=0;i<=t;i++)
		  { 
			System.out.print("\n\nVariation "+i+" :");
			System.out.print("                                            "+variation[i].fw);
			  System.out.print("     "+variation[i].fv);
			  System.out.print("     "+variation[i].fsw);

			
			for(j=0;j<variation[i].cSize;j++)
			{
			  if(variation[i].co[j]!=null)
			  {
				  System.out.print("\nV.Candidate Object "+j+" :");
				  System.out.print("        "+variation[i].co[j].name);
				  System.out.print("        "+variation[i].co[j].weight);
				  System.out.print("        "+variation[i].co[j].value);


			  }
		
			 else
			 {
				  System.out.println("\n null");
			 }
			
           
			}//end of for j
			  
		  }//end of for i

      */
        //LEARNING BEGINS

		
		 //let each variation learn from the followee 

		//if Feasible or not
			
			
              if(follower.feasible==true)
			  {
				for(k=1;k<=t;k++)
				{
				System.out.println("\n\nVARIATION "+k);
				//randomly choose between adding(0) or replacing(1)
				rChoice = rand.nextInt(2);
				System.out.print("\nChoice selected "+rChoice);
                
			    	if(rChoice==0)//adding from followee
                    {
						added=false;
						System.out.println(" ie. Will add from followee");  
					    attempt=0;
					  while(added==false && attempt<20)
					 {
					  //randomly select an object from followee and add it to the variation
	    			    attempt++;
						oaChoice=rand.nextInt(followee.objectCount);
                        //System.out.println("Object selected from followee : "+followee.co[oaChoice].name);
                         int e=0;
						 boolean exists=false;
						//to check if the object exists in candidate or any of its variations so far
						while(e<k && exists==false)
						{
			    	       exists=variation[e].ifExists(followee.co[oaChoice].name);
						   e++;
                        }
                         
                        float newWeight=follower.fw+followee.co[oaChoice].weight;
                       // System.out.println("newWeight = "+newWeight+"Exists = "+exists);
					    if(newWeight<=Candidate.W && exists==false)
					    {
						  added=true;
						  variation[k].addObject(followee.co[oaChoice]);
						  System.out.println("Finally added "+followee.co[oaChoice].name);
					    }
					  }//end of while
					 
				     }//end of rchoice==0
				    else if(rChoice==1)//replacing from followee
				     {
			            //here first randomly remove from follower variation and then randomly add from followee 	
						System.out.println("  ie. Will replace from followee");
                        
						//random removal of object from follower
						orChoice=rand.nextInt(follower.objectCount);												
					//	System.out.println("Object to be removed from follower : "+follower.co[orChoice].name);
						
						
						//random addition of object from followee
                        added=false;
						attempt=0;
					
					    while(added==false  && attempt<20)
						{
						  attempt++;
	    				  oaChoice=rand.nextInt(followee.objectCount);
                       //   System.out.println("Object selected from followee "+followee.co[oaChoice].name);
                        
						  //int e=0;
						  boolean exists=false;
						//  while(e<k && exists==false)
						  //{
							
							String s=followee.co[oaChoice].name;


		         	       exists=variation[0].ifExists(s);
						  // e++;
                          //}
                          
			    	      
                          float newWeight=follower.fw+followee.co[oaChoice].weight-follower.co[orChoice].weight;

					      if(newWeight<=Candidate.W && exists==false)
					      {
						    added=true;
						    variation[k].addObject(followee.co[oaChoice]);
							variation[k].removeObject(orChoice);
							System.out.print("\nFinally replaced "+follower.co[orChoice].name);
							System.out.println("with "+followee.co[oaChoice].name);
                            
					      }
						}//end of while
						
	               	 }//end of if rchoice==1
                     
					 float tempv=variation[k].calculateFw();
					  float tempw=variation[k].calculateFv();
				}//end of for k(variations)
			}//end of if feasible=true
			else if(follower.feasible==false)
			{
               int choiceCount=0;
			   for(int h=0;h<40;h++)
				{
				 choiceMemory[h]=666;
				}
		       
               for(k=1;k<=t;k++)
			   {
				System.out.println("\n\nVARIATION "+k);
				//randomly choose between removing(0) or replacing(1)
				rChoice = rand.nextInt(2);
				System.out.print("\nChoice selected "+rChoice);
                
			    	if(rChoice==0)//removing from follower
                    {
	                    						
						  System.out.println("  ie. Will remove from follower");  					  
					      boolean selected=false;
						  attempt=0;
                          orChoice=777;
						  while(selected==false &&attempt<10)
						  {
						   attempt++;
						   orChoice=rand.nextInt(follower.objectCount);
						   i=0;
						   boolean removedBefore=false;
						   while(removedBefore==false && i<=choiceCount)
						   {
							   i++;
							  if(orChoice==choiceMemory[i])
							  {
								  removedBefore=true;
								  break;
							  }
						   }
						   if(removedBefore==false)
						   {							 
			                 choiceMemory[choiceCount]=orChoice;
							 choiceCount++;
							 selected=true;
						    }
						  }//while loop
						  if(selected==true && orChoice!=777)
						 {
					      System.out.println("Object removed from follower : "+follower.co[orChoice].name);
                          variation[k].removeObject(orChoice);
						 }
					
					    

				     }//end of rchoice==0
				     else if(rChoice==1)//replacing from followee
				     {
			            //here first randomly remove from follower variation and then randomly add from followee 	
						System.out.println("  ie. Will replace from followee");
                        
						//random removal of object from follower
						orChoice=rand.nextInt(follower.objectCount);												
						//System.out.println("Object to be removed from follower : "+follower.co[orChoice].name);
						
						
						//random addition of object from followee
                        added=false;
						attempt=0;
					
					     while(added==false  && attempt<20)
						 {
						  attempt++;
	    				  oaChoice=rand.nextInt(followee.objectCount);
                       //   System.out.println("Object to be added from followee "+followee.co[oaChoice].name);
						  //int e=0;
						  boolean exists=false;
						  
						  String s= followee.co[oaChoice].name;
			    	      exists=variation[0].ifExists(s);
						 
			    	      
                          float newWeight=follower.fw+followee.co[oaChoice].weight-follower.co[orChoice].weight;
                      //    System.out.println("\nnewWeight = "+newWeight);
					      if(newWeight<=Candidate.W && exists==false)
					      {
						    added=true;
						    variation[k].addObject(followee.co[oaChoice]);
							variation[k].removeObject(orChoice);
							System.out.print("\nFinally replaced "+follower.co[orChoice].name);
							System.out.println("with "+followee.co[oaChoice].name);
					      }

						}//end of while
	               	 }//end of if rchoice==1
                  
				  float tempv=variation[k].calculateFw();
				  float tempw=variation[k].calculateFv();
				}//end of for k(variations)

             
        
			}//end of if feasible =false
			
		  
		  
	
		
		/*  
		 
        //Display Variations 

		  System.out.println("\nVariation                  Name       Weight      Value     fw    fv	 fsw"); 
		  for(i=0;i<=t;i++)
		  { 
			System.out.print("\n\nVariation "+i+" :");
			System.out.print("                                            "+variation[i].fw);
			  System.out.print("     "+variation[i].fv);
			  System.out.print("     "+variation[i].fsw);

			
			for(j=0;j<variation[i].cSize;j++)
			{
			  if(variation[i].co[j]!=null)
			  {
				  System.out.print("\nV.Candidate Object "+j+" :");
				  System.out.print("        "+variation[i].co[j].name);
				  System.out.print("        "+variation[i].co[j].weight);
				  System.out.print("        "+variation[i].co[j].value);


			  }
			/*
			 else
			 {
				  System.out.println("\n null");
			 }
			
            
			}//end of for j
			  
		  }//end of for i
    */

     
   //Selection of best variation as the new state of Candidate
          //Sorting of candidates
          Candidate temp;
          for(i=0;i<=t;i++)
		  {
			  for(j=i+1;j<=t;j++)
			  {
				  if((variation[j].fsw>variation[i].fsw)||(variation[j].fsw==variation[i].fsw &&( (variation[i].fsw==1 && variation[j].fv>variation[i].fv) || (variation[i].fsw==0 && variation[j].fw<variation[i].fw) ) ) )
				  {
                     temp=variation[i];
					 variation[i]=variation[j];
					 variation[j]=temp;
				  }
			  }
		  }

		  //Display Variations after sorting

		         //Display Variations 

		  System.out.println("\n\n\nVariation                  Name       Weight      Value     fw    fv	 fsw"); 
		  for(i=0;i<=t;i++)
		  { 
			System.out.print("\n\nVariation "+i+" :");
			System.out.print("                                            "+variation[i].fw);
			  System.out.print("     "+variation[i].fv);
			  System.out.print("     "+variation[i].fsw);

			
			for(j=0;j<variation[i].cSize;j++)
			{
			  if(variation[i].co[j]!=null)
			  {
				  System.out.print("\nV.Candidate Object "+j+" :");
				  System.out.print("        "+variation[i].co[j].name);
				  System.out.print("        "+variation[i].co[j].weight);
				  System.out.print("        "+variation[i].co[j].value);


			  }
			/*
			 else
			 {
				  System.out.println("\n null");
			 }
			
            */
			}//end of for j
			  
		  }//end of for i

     //Select the Best Variation and make it as the new state of the follower Candidate
     
	 //Best variation is at variation[0]

	 for(i=0;i<follower.cSize;i++)
	 {
		 follower.co[i]=null;
		 if(variation[0].co[i]!=null)
		 {
			 follower.co[i]=variation[0].co[i];
		 }
		 
	 }
    follower.fw=variation[0].fw;
	follower.fv=variation[0].fv;
	follower.objectCount=variation[0].objectCount;
    
	//Display new Follower State

		  System.out.println("\nCandidate             Name   Weight   Value   Feasibility  Object Count"); 
		   
              System.out.println("\n\nCandidate "+follower.name+" :");
			  System.out.print("                                                 "+follower.feasible);
			  System.out.print("       "+follower.objectCount);
			for(j=0;j<follower.cSize;j++)
			{
			  if(follower.co[j]!=null)
			  {
				  System.out.print("\nCandidate Object "+j+" :");
				  System.out.print("       "+follower.co[j].name);
				  System.out.print("     "+follower.co[j].weight);
				  System.out.print("     "+follower.co[j].value);
				    
			  }
			  /*else
			  {
				  System.out.println("\n null");
			  }
			 */

			}//end of for j

			//Make variations array all clear for reuse by next candidate
			  
			  for(k=0;k<=t;k++)//for each variation
			 {
                 for(int p=0;p<variation[k].cSize;p++)//for each object in follower to be added to variation
			   	 {
					 variation[k].co[p]=null;
				 }
				 variation[k].fv=0;
				 variation[k].fw=0;
				 variation[k].p=0;
			 }//end of for k


	}//all candidates have been updated at the end of this loop
     
	 //Display Candidates

  	
		   //now calculate fv and fw of each candidate and 
		  //and as we do that, calculate fvSum and fwSum of the Cohort
	     cohort.fwSum=0;
		 cohort.fvSum=0;
		  for(i=0;i<cohortSize;i++)
		  {
			  //fwSum
				cohort.fwSum=cohort.fwSum+cohort.candidate[i].calculateFw();
			  //fvSum
				cohort.fvSum=cohort.fvSum+cohort.candidate[i].calculateFv();
		  }	
		  
		
		
		//Display Candidates 

		  System.out.println("\nCandidates              Name   Weight   Value   Feasibility  Object Count"); 
		  for(i=0;i<cohortSize;i++)
		  { 
              System.out.println("\n\nCandidate "+i+" :");
			  System.out.print("                                                 "+cohort.candidate[i].feasible);
			  System.out.print("       "+cohort.candidate[i].objectCount);
			for(j=0;j<cohort.candidate[i].cSize;j++)
			{
			  if(cohort.candidate[i].co[j]!=null)
			  {
				  System.out.print("\nCandidate Object "+j+" :");
				  System.out.print("       "+cohort.candidate[i].co[j].name);
				  System.out.print("     "+cohort.candidate[i].co[j].weight);
				  System.out.print("     "+cohort.candidate[i].co[j].value);
				    
			  }
			  /*else
			 {
				  System.out.println("\n null");
			 }
			 */

			}//end of for j
			  
		}//end of for i
	   
	
		  System.out.println("\n\nCohort's fwSum ="+cohort.fwSum);
		  System.out.println("Cohort's fvSum= "+cohort.fvSum);

		  
		 
	
	  // finding the best and worst candidate
          for(i=0;i<cohort.size;i++)
		  {
			  rankArray[i]=i;
		  }
		  //logic for maintaining rank
		  
		  for(i=0;i<cohort.size;i++)
		  {
			  for(j=i+1;j<cohort.size;j++)
			  {
				  if(cohort.candidate[j].fv>cohort.candidate[i].fv)
				  {
                    tem=rankArray[i];
					rankArray[i]=rankArray[j];
					rankArray[j]=tem;
				  }
			  }
		  }
		  
          
		  pbestCandidate=bestCandidate;
		  pworstCandidate=worstCandidate;
		  bestCandidate=cohort.candidate[rankArray[0]];
		  worstCandidate=cohort.candidate[rankArray[cohort.size-1]];
		  System.out.println("\n\nPreviousBest Candidate = "+pbestCandidate.name+" with fv ="+pbfv);
          System.out.println("Previous Worst Candidate = "+pworstCandidate.name+" with fv ="+pwfv);
		  
		  System.out.println("\nBest Candidate = "+bestCandidate.name+" with fv ="+bestCandidate.fv);
          System.out.println("Worst Candidate = "+worstCandidate.name+" with fv ="+worstCandidate.fv);
	      
	      float e1,e2,e3;
		  e1=Math.abs(bestCandidate.fv-worstCandidate.fv);
		  e2=Math.abs(bestCandidate.fv-pbfv);
		  e3=Math.abs(worstCandidate.fv-pwfv);

		  if((e1<0.001 && (e2<0.001 || e3<0.001)) || (e2<0.001 && (e1<0.001 || e3<0.001))|| (e3<0.001 && (e2<0.001 || e1<0.001)))
		  {
			  saturation=true;
		  }
		  pbfv=bestCandidate.fv;
		  pwfv=worstCandidate.fv;
	  
	  }
     
	  
	//Display new Follower State
          System.out.println("\n\n        Optimum Solution");
		  System.out.println("\n\n                Name   Weight   Value   Feasibility  Object Count"); 
		   
              System.out.println("\n\nCandidate "+bestCandidate.name+" :");
			  System.out.print("                                                 "+bestCandidate.feasible);
			  System.out.print("       "+bestCandidate.objectCount);
			for(j=0;j<bestCandidate.cSize;j++)
			{
			  if(bestCandidate.co[j]!=null)
			  {
				  System.out.print("\nCandidate Object "+j+" :");
				  System.out.print("       "+bestCandidate.co[j].name);
				  System.out.print("     "+bestCandidate.co[j].weight);
				  System.out.print("     "+bestCandidate.co[j].value);
				    
			  }
			  /*else
			  {
				  System.out.println("\n null");
			  }
			 */

			}//end of for j
	 
	 System.out.println("\n\nBest Value = "+bestCandidate.calculateFv());
	 System.out.println("\nBest Weight = "+bestCandidate.calculateFw());
	 
	 System.out.println("\n\nDo you want to change candidate and variation parameters and try again?(Y/N)");
     ans=in.next();



	}while(ans.equals("y") || ans.equals("Y"));
     
	}//end of main method




		 public static void populateCandidate(Cohort cohort,Objects[] o)
		 {		
		  // System.out.println("\nCame inside Populate Candidate method");
           int i,now,oCount;
		   now=0;
		   float wAdded=0;
		   int oRankArray[]=new int[Cohort.N];
		   for(i=0;i<Cohort.N;i++)
			 {
			   oRankArray[i]=i;
			 }


           //adding unused objects to the candidates
		   for(i=0;i<cohort.size;i++)
		   { 
			   wAdded=0;
		       oCount=0;
			   float originalWeight=(Candidate.W)*(cohort.po/100);
			//System.out.println("Original Weight ="+originalWeight);
			
			while(wAdded<=originalWeight && oCount<Cohort.N)
			   { 
                // System.out.println("\nCame inside while loop of for of Populate Candidate method");
			      oCount++;
				  now=getNextObject(o,cohort,oRankArray);
				  //System.out.println("now = "+now);
				  //System.out.println("o[now].useCount = "+o[now].useCount);
                 //System.out.println("\nCame back from getNextObject method");
                 if(o[now].useCount>0 || (wAdded+o[now].weight)>originalWeight)
				 {
					 break;
				 }
                 if(o[now].useCount==0)
				  {
					// System.out.println("Got inside if of o[now].useCount==0 ");
					 cohort.candidate[i].addObject(o[now]);
					 o[now].useCount++;
					 wAdded=wAdded+o[now].weight;
					 //System.out.println("wAdded="+wAdded);
				  }


				  
			   }//end of while
              
		   }//end of for

           //Display Candidates 

		  System.out.println("\nCandidates              Name   Weight   Value   Feasibility  Object Count"); 
		  for(int h=0;h<cohort.size;h++)
		  { 
              System.out.println("\n\nCandidate "+h+" :");
			  System.out.print("                                                 "+cohort.candidate[h].feasible);
			  System.out.print("       "+cohort.candidate[h].objectCount);
			for(int j=0;j<cohort.candidate[h].cSize;j++)
			{
			  if(cohort.candidate[h].co[j]!=null)
			  {
				  System.out.print("\nCandidate Object "+j+" :");
				  System.out.print("       "+cohort.candidate[h].co[j].name);
				  System.out.print("     "+cohort.candidate[h].co[j].weight);
				  System.out.print("     "+cohort.candidate[h].co[j].value);
				    
			  }
			  /*else
			 {
				  System.out.println("\n null");
			 }
			 */

			}//end of for j
			  
		}//end of for i
		  

       //Fill the remaining part of Candidates to make them feasible solution

	      for(i=0;i<cohort.size;i++)
		   { 
			   float candidateWeight=cohort.candidate[i].calculateFw();
		       oCount=0;
			   
			//System.out.println("Original Weight ="+originalWeight);
			
			while(candidateWeight<=Candidate.W && oCount<Cohort.N)
			   {
			      oCount++;
				 
				  boolean exists=false;
				  now=getNextObject(o,cohort,oRankArray);
			  
				  exists=cohort.candidate[i].ifExists(o[now].name);						 
                  
                 if((candidateWeight+o[now].weight)<((Candidate.W)*1.2) && exists==false)
				 {
					 cohort.candidate[i].addObject(o[now]);
					 o[now].useCount++;				
				 }
                 					
                 candidateWeight=cohort.candidate[i].calculateFw();
				  
			   }//end of while
              
		   }//end of for


		/*	//temporary code where i am entering objects in candidate manually
			if(c.name==0)
			{
				c.addObject(o[0]);
				c.addObject(o[1]);
				c.addObject(o[3]);
				c.addObject(o[4]);
				
			}
			else if(c.name==1)
			{
				c.addObject(o[0]);
				c.addObject(o[1]);
				c.addObject(o[2]);
				c.addObject(o[5]);
			}
			else if(c.name==2)
			{
				c.addObject(o[0]);
				c.addObject(o[3]);
		   		c.addObject(o[2]);
				c.addObject(o[6]);
			}
          */
		} //end of populateCandidate
      
	  public static int getNextObject(Objects[] o,Cohort cohort,int[] oRankArray)
	  {
		//System.out.println("\nCame inside getNextObject method");
		int i,j,temp;
	    for(i=0;i<Cohort.N;i++)
		{
			for(j=i+1;j<Cohort.N;j++)
			{
				if(o[oRankArray[j]].useCount<o[oRankArray[i]].useCount)
				{
					temp=oRankArray[i];
					oRankArray[i]=oRankArray[j];
					oRankArray[j]=temp;
				}
			}

		}
		

		int bestObject=oRankArray[0];
	    o[oRankArray[0]].chosenCount++;
         for(i=0;i<Cohort.N;i++)
		 {
			for(j=i+1;j<Cohort.N;j++)
			{
				if(o[oRankArray[j]].chosenCount<o[oRankArray[i]].chosenCount)
				{
					temp=oRankArray[i];
					oRankArray[i]=oRankArray[j];
					oRankArray[j]=temp;
				}
			}//eo for j

		  }//end of for i
        
		return bestObject;

	  }

	  public static void probabilityDistribution(Cohort cohort,float W)
	  {
		  for(int i=0;i<cohort.size;i++)
		  {
			System.out.print("\nP for candidate "+cohort.candidate[i].name+" : ");
			cohort.pArray[i]=cohort.candidate[i].calculateP(cohort.fvSum,W);
			System.out.print(cohort.pArray[i]);
		  }		

	  }//end of probabilityDistribution method

		
	  public static int findFollowee(float[] pArray,int pSize)
	  {
		Random rand=new Random();
		float randNo=0;
		float pSum=0;
		int i;
		//boolean yes=false;
		for(i=0;i<pSize;i++)
		{
			pSum=pSum+pArray[i];
		}

		float r=rand.nextInt(Math.round(pSum*100));
		randNo=r/100;
		//System.out.print("(RandNo : "+randNo+")");
		float lower,upper;
		lower=0;
		upper=pArray[0];
        i=0;

		do
		{
		  if(randNo>lower && randNo<=upper)
		  {
			 // yes=true;
			  break;
		  }
		  else
		   {
			  i++;
			  lower=upper;
			  upper=upper+pArray[i];
		   }
			  
		}while(i<(pSize-1));//end of while
		 
		 return i;
		  
	  }//end of find followee
		
			
    
};//end of class CIKnapsack