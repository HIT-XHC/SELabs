package SELabs.Lab1;

//�������Lab1�ķ�������㷨

//testx

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ServiceComposition {
	
/*
* �㷨˼·��
* 1.����Ҫдһ����������ȡ���нڵ�ĺ�ѡ���񼯺�
* 
* */
	
//�ļ������еĺ�ѡ���񣬲����Ƿ��õ�����������Ϊ�˴����ļ����㡣
List<List<CandidateService>> CandidateServices= new ArrayList<List<CandidateService>>();  

//����Process��ÿ���ڵ��Ӧ�ĺ�ѡ���񼯣�ע�⣺���㷨����ÿ���ڵ���������ֻ����һ�Σ�
List<List<CandidateService>> NodesCandidateServices= new ArrayList<List<CandidateService>>();  

double reqReliability=0;  //����Ŀɿ���Լ��
double reqPrice=0;  //����ļ۸�Լ��

List<List<String>> Process= new ArrayList<List<String>>();  //����
List<String> Nodes= new ArrayList<String>();  //�����г��ֹ��Ľڵ�

List<List<CandidateService>> allSolutions= new ArrayList<List<CandidateService>>();   //��������������Ϸ���
List<CandidateService> bestSolution= new ArrayList<CandidateService>();   //��������������Ϸ���

double ResultReliability = 0; //��������Ӧ�Ŀɿ���ֵ
double ResultPrice = 0;//��������Ӧ�ļ۸�ֵ
double ResultObject= -99999;//��������Ӧ��Ŀ�꺯��ֵ
int ResultIndex=0; //���ŵ���Ϸ�������ӦResult�е����

public ServiceComposition()
{	
	//��ʼ����ѡ�����б�,�ٶ�����������20��
	for(int i=1;i<=20;i++)
	{
		List<CandidateService> tempC = new ArrayList<CandidateService>();
		CandidateServices.add(tempC);
		}
	

	//��ʼ�����������б�,�ٶ�����������20��
	for(int i=1;i<=20;i++)
	{
		List<String> tempC = new ArrayList<String>();
		this.Process.add(tempC);
		}

	}


/*�˷����������ļ�service.txt�ж�ȡ���еĺ�ѡ���񣬱�����CandidateServices��*/

public void getAllCandidateServices()
{	
	String filename="c://service.txt"; 
	File file = new File(filename);
	if (!file.exists()) {
		System.out.println("Error!File doesn't exist!");
		return;
	}
	else
	{
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String tmpLine =br.readLine();
			while(tmpLine!=null){
				String tmpArray[] = tmpLine.split(" ");
				if (tmpArray.length != 5) {
					break;
				}
				
				CandidateService tmpCandidate=new CandidateService();
				tmpCandidate.serviceID=tmpArray[0];  //��ѡ������
								
				tmpCandidate.serviceReliability=Double.parseDouble(tmpArray[2]);  //�ɿ���
				tmpCandidate.servicePrice=Double.parseDouble(tmpArray[4]);  //�۸�

				//tmpCandidate.CalculateQ();  
				tmpCandidate.serviceActivityID=(tmpArray[0].split("-"))[0]; //��Ӧ�ķ���ID
				
				//���IDת��Ϊ���֣�A=0��B=1...
				char tmpA='A';
				tmpCandidate.serviceActivityNumID=(int)(tmpCandidate.serviceActivityID.charAt(0))-(int)tmpA;
				//����ǰ�ķ��񱣴���ָ��λ��
				CandidateServices.get(tmpCandidate.serviceActivityNumID).add(tmpCandidate);
								
				tmpLine = br.readLine(); //������ȡ��һ��
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	}


/*
* ���ļ��ж�ȡ���󣬲��������˵ڼ�������
* */
public void getReq(int lineNumber)
{
	String filename="c://req.txt"; 
	File file = new File(filename);
	if (!file.exists()) {
		System.out.println("Error!File doesn't exist!");
		return;
	}
	else
	{
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String tmpLine =br.readLine();
			int tmpLineNum=1;
			while(tmpLine!=null){
				if (tmpLineNum==lineNumber)
				{
					String tmpArray[] = tmpLine.split(",");
					if (tmpArray.length != 2) {
						break;
					}
					else
					{
						this.reqReliability=Double.parseDouble(tmpArray[0].substring(1));
						this.reqPrice=Double.parseDouble(tmpArray[1].substring(0,tmpArray[1].length()-1));
						break;
					}
									
				}
				else
				{
					tmpLineNum++;
					tmpLine = br.readLine(); //������ȡ��һ��
				}
				
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	}

/**
* �˺���������ȡ���̶�
* @param lineNumber �����˵ڼ�������
* 
* ���̴洢���õĸ�ʽΪ��άlist����1ά�洢����ÿ�����������ֱ���ӽڵ㣻��2ά�洢�������нڵ���б�
* 
*/
public void getProcess(int lineNumber)
{
	String filename="c://process.txt"; 
	File file = new File(filename);
	if (!file.exists()) {
		System.out.println("Error!File doesn't exist!");
		return;
	}
	else
	{
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String tmpLine =br.readLine();
			int tmpLineNum=1;
			while(tmpLine!=null){
				if (tmpLineNum==lineNumber)
				{
					String tmpArray[] = tmpLine.split("\\),");
					for(int i=0;i<tmpArray.length;i++)				
					{
						char tmpA='A';
						String tmpNode=tmpArray[i].charAt(3)+"";
						//�ѽڵ�洢���丸�ڵ��Ӧ���б���
						this.Process.get((int)tmpArray[i].charAt(1)-(int)tmpA).add(tmpNode);
						
						//��ȡ�������еĽڵ㣬��¼������Щ�ڵ�����ˣ�Ϊ�������㷨��׼��
						if (Nodes.indexOf(tmpArray[i].charAt(1)+"")==-1)  //���ڵ�
							Nodes.add(tmpArray[i].charAt(1)+"");
						if (Nodes.indexOf(tmpArray[i].charAt(3)+"")==-1)  //�ӽڵ�
							Nodes.add(tmpArray[i].charAt(3)+"");
					
					}
					
					break;
									
				}
				else
				{
					tmpLineNum++;
					tmpLine = br.readLine(); //������ȡ��һ��
				}
				
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}



/*
*
* Step1���ȴ����ѡ���񼯺ϣ�����Nodes�еĽڵ㣬�ҵ�ÿ���ڵ��Ӧ�ĺ�ѡ����
* ���Ϊ�˽�ʡʱ�䣬���㷨�����ںϵ�getCandidateServices�㷨�У�����Ҫ��getProcess�㷨֮��
* */

public void getNodesCandidateServices()
{   
	//Step1��������ÿ���ڵ��Ӧ�ĺ�ѡ���񼯣�ע�⣺���㷨����ÿ���ڵ���������ֻ����һ�Σ�
	//�Ӻ�ѡ����ȫ���У��ҵ������������нڵ�����к�ѡ����
  for(int i=0;i<this.Nodes.size();i++)
  {
  	List<CandidateService> tmpService=null;
  	char tmpA='A';
  	int tmpIndex=(int)Nodes.get(i).charAt(0)-(int)tmpA;
  	tmpService=this.CandidateServices.get(tmpIndex);
  	this.NodesCandidateServices.add(tmpService);
  }
 
  
  //�˶δ�������������ѡ�������Ŀ���ǳ���Ҫ�����������á�  ͨ������j��ֵ�����ƣ���j=500�Ǵ��������еġ�
  List<List<CandidateService>> tmpResult= new ArrayList<List<CandidateService>>();   //������
  for(int i=0;i<this.Nodes.size();i++)
  {
  	List<CandidateService> Result= new ArrayList<CandidateService>();   //������
  	
  	for(int j=0;j<50;j++)
  	{
  		Result.add(this.NodesCandidateServices.get(i).get(j));
  		

  	}
  	tmpResult.add(Result);
  }
	   
  this.NodesCandidateServices=tmpResult;
}


/*
* ������ϵ������������õݹ��㷨
* 
* layer ����ָ����ǰ�Ĳ���
* curComposition �������浱ǰ�����Ƭ��
*
* ���㷨����������Ϸ���
* */

public void compositionAll(int layer, List<CandidateService> curComposition,double curReliability,double curPrice)
{   
	//ʣ�����һ������ʱ��
  if (layer < this.NodesCandidateServices.size() - 1)
  {
    for (int i = 0; i < this.NodesCandidateServices.get(layer).size(); i++)
    {
  	  
  	  //�������ʽӦ����һ�����飬��������ÿ��ѡ��ķ���
  	  List<CandidateService> tmpResult= new ArrayList<CandidateService>();  //������ʱ���
        tmpResult.addAll(curComposition);
        tmpResult.add(NodesCandidateServices.get(layer).get(i));
        
        //�õ���ǰ��ϵ�QoSֵ
        double tmpRe = curReliability * NodesCandidateServices.get(layer).get(i).serviceReliability;
        double tmpPr = curPrice + NodesCandidateServices.get(layer).get(i).servicePrice;
        
        //��֦����
        if(tmpPr<=this.reqPrice && tmpRe>=this.reqReliability)
      	  compositionAll(layer + 1, tmpResult,tmpRe,tmpPr);
        }
     
  }
  
  //ʣ��ֻ��һ�����ϣ��ڵ㣩ʱ��
  else if (layer == this.NodesCandidateServices.size() - 1)
  {
     
          for (int i = 0; i < this.NodesCandidateServices.get(layer).size(); i++)
          {
          	
        	    List<CandidateService> tmpResult= new ArrayList<CandidateService>();  //������ʱ���
              tmpResult.addAll(curComposition);
              tmpResult.add(NodesCandidateServices.get(layer).get(i));
              
              double tmpRe = curReliability * NodesCandidateServices.get(layer).get(i).serviceReliability;
              double tmpPr = curPrice + NodesCandidateServices.get(layer).get(i).servicePrice;
              
              
              if(tmpPr<=this.reqPrice && tmpRe>=this.reqReliability)
              	this.allSolutions.add(tmpResult);
              //�˴��������Ż��ĵط������жϵ�ǰ����Ϸ���Ŀ�꺯��ֵ�Ƿ���ڵ�ǰ�����ţ������򱣴棬����ȥ����
             
          }                
  } 
}





/*
* ������ϵ������������õݹ��㷨
* 
* layer ����ָ����ǰ�Ĳ���
* curComposition �������浱ǰ�����Ƭ��
*
* ���㷨ֻ��������
* */

public void compositionBest(int layer, List<CandidateService> curComposition,double curReliability,double curPrice)
{   
	//ʣ�����һ������ʱ��
  if (layer < this.NodesCandidateServices.size() - 1)
  {
    for (int i = 0; i < this.NodesCandidateServices.get(layer).size(); i++)
    {
  	  
  	  //�������ʽӦ����һ�����飬��������ÿ��ѡ��ķ���
  	  List<CandidateService> tmpResult= new ArrayList<CandidateService>();  //������ʱ���
        tmpResult.addAll(curComposition);
        tmpResult.add(NodesCandidateServices.get(layer).get(i));
        
        //�õ���ǰ��ϵ�QoSֵ
        double tmpRe = curReliability * NodesCandidateServices.get(layer).get(i).serviceReliability;
        double tmpPr = curPrice + NodesCandidateServices.get(layer).get(i).servicePrice;
        
        //��֦����
        if(tmpPr<=this.reqPrice && tmpRe>=this.reqReliability)
      	  compositionBest(layer + 1, tmpResult,tmpRe,tmpPr);
        }
     
  }
  
  //ʣ��ֻ��һ�����ϣ��ڵ㣩ʱ��
  else if (layer == this.NodesCandidateServices.size() - 1)
  {
     
          for (int i = 0; i < this.NodesCandidateServices.get(layer).size(); i++)
          {
          	
        	    List<CandidateService> tmpResult= new ArrayList<CandidateService>();  //������ʱ���
              tmpResult.addAll(curComposition);
              tmpResult.add(NodesCandidateServices.get(layer).get(i));
              
              double tmpRe = curReliability * NodesCandidateServices.get(layer).get(i).serviceReliability;
              double tmpPr = curPrice + NodesCandidateServices.get(layer).get(i).servicePrice;
              
             
              if(tmpPr<=this.reqPrice && tmpRe>=this.reqReliability) //�ж��Ƿ�����Լ��
              {
           	                	                	            		
          		if ((tmpRe-tmpPr/100)>this.ResultObject)
          		{
          			//this.ResultIndex = i;
          			this.ResultObject = tmpRe-tmpPr/100;
          			this.ResultPrice = tmpPr;
          			this.ResultReliability = tmpRe;
                  	this.bestSolution = tmpResult;
          		}            		
             	
              }
             
          }                
  } 
}



/**
*���з�������㷨 
*/

public void runSC(ServiceComposition NewSC)
{   
	//ServiceComposition NewSC=new ServiceComposition();
	
	//��ֵ����Ҫ���������
	int lineNumber=4;
	
	
	
	//���ļ��ж�ȡ���еĺ�ѡ����
	NewSC.getAllCandidateServices();  

	//���ļ��л�ȡ���󣬲��������˵ڼ�������
	NewSC.getReq(lineNumber);
	System.out.println(NewSC.reqPrice);
	System.out.println(NewSC.reqReliability);
	
	//��ȡ����
	NewSC.getProcess(lineNumber);
	/*
	for(int i=0;i<NewSC.Nodes.size();i++)  //������
	{
		System.out.println(NewSC.Nodes.get(i));
	}
	*/
	
	//��ȡÿ���ڵ�ĺ�ѡ���񼯺�
	NewSC.getNodesCandidateServices();
	/*
	for(int i=0;i<NewSC.NodesCandidateServices.size();i++) //������
	{
		for(int j=0;j<NewSC.NodesCandidateServices.get(i).size();j++)
		
			System.out.println(NewSC.NodesCandidateServices.get(i).get(j).serviceID);
	}*/
	
	//���÷�������㷨���ҵ����п��еķ���
	List<CandidateService> curComposition= new ArrayList<CandidateService>();
	NewSC.compositionBest(0,curComposition,1,0);
	
	NewSC.printBestSolution(NewSC);
		
	
}

/**
*�����п��з�����Ϸ������ҵ����Ž� ����� compositionAll�����㷨һ����
*/

public void findBestSolution(ServiceComposition NewSC)
{   
	//double maxObj=-99999;  //����Ŀ�꺯��ֵ
 // int optIndex=0;        //���ŵķ�����Ϸ������ 
  
	//��ӡ������Ϸ�������Ӧ��ֵ
	for(int i=0;i<NewSC.allSolutions.size();i++)
	{   
		double tmpre=1;
		double tmppr=0;
	
		for(int j=0;j<NewSC.allSolutions.get(i).size();j++)
		{
			tmpre=tmpre*NewSC.allSolutions.get(i).get(j).serviceReliability;
			tmppr=tmppr + NewSC.allSolutions.get(i).get(j).servicePrice;
			//System.out.print(NewSC.Result.get(i).get(j).serviceID+"  "); �����Ҫ��ӡ������ȥ��ע��
		}
		
		if ((tmpre-tmppr/100)>this.ResultObject)
		{
			this.ResultIndex = i;
			this.ResultObject = tmpre-tmppr/100;
			this.ResultPrice = tmppr;
			this.ResultReliability = tmpre;
		}
		
		//System.out.print(tmpre+"  "+tmppr+"  "+(tmpre-tmppr/100));
	}
	
	//��ӡ���ŷ���
	System.out.println("���Ž�Ϊ��");
	for(int j=0;j<NewSC.allSolutions.get(this.ResultIndex).size();j++)
	{
		System.out.print(NewSC.allSolutions.get(this.ResultIndex).get(j).serviceID+"  ");
	}
	
	System.out.println("����ֵ��"+this.ResultObject);
	System.out.println("�ɿ��ԣ�"+this.ResultReliability);
	System.out.println("�۸�" +this.ResultPrice);
	System.out.println(NewSC.allSolutions.size());
	
}

/*
* ��ӡ������ֵ�����compositionBest()�㷨ʹ��
* 
*/


public void printBestSolution(ServiceComposition NewSC)
{   
	  

	if (this.ResultObject!=-99999)  //������ֵ
	{
		//��ӡ���ŷ���
		System.out.println("���Ž�Ϊ��");
		for(int j=0;j<this.bestSolution.size();j++)
		{
			System.out.print(this.bestSolution.get(j).serviceID+"  ");
		}
		
		System.out.println("����ֵ��"+this.ResultObject);
		System.out.println("�ɿ��ԣ�"+this.ResultReliability);
		System.out.println("�۸�" +this.ResultPrice);
				
	}
	else
		System.out.println("û�����Ž⣡");

}


public static void main(String[] args) {

	long beginTime = System.currentTimeMillis();
	ServiceComposition NewSC=new ServiceComposition();
	NewSC.runSC(NewSC);
	
	long endTime = System.currentTimeMillis();
	
	System.out.println("Run time:" +(endTime-beginTime));
	

}

}
