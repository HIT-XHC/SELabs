package SELabs.Lab1;

//软件工程Lab1的服务组合算法

//testx

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ServiceComposition {
	
/*
* 算法思路：
* 1.首先要写一个方法来读取所有节点的候选服务集合
* 
* */
	
//文件中所有的候选服务，不管是否用到；这样做是为了处理文件方便。
List<List<CandidateService>> CandidateServices= new ArrayList<List<CandidateService>>();  

//流程Process中每个节点对应的候选服务集（注意：本算法假设每个节点在流程中只运行一次）
List<List<CandidateService>> NodesCandidateServices= new ArrayList<List<CandidateService>>();  

double reqReliability=0;  //需求的可靠性约束
double reqPrice=0;  //需求的价格约束

List<List<String>> Process= new ArrayList<List<String>>();  //流程
List<String> Nodes= new ArrayList<String>();  //流程中出现过的节点

List<List<CandidateService>> allSolutions= new ArrayList<List<CandidateService>>();   //保存结果，所有组合方案
List<CandidateService> bestSolution= new ArrayList<CandidateService>();   //保存结果，所有组合方案

double ResultReliability = 0; //保存结果对应的可靠性值
double ResultPrice = 0;//保存结果对应的价格值
double ResultObject= -99999;//保存结果对应的目标函数值
int ResultIndex=0; //最优的组合方案，对应Result中的序号

public ServiceComposition()
{	
	//初始化候选服务列表,假定服务活动不超过20个
	for(int i=1;i<=20;i++)
	{
		List<CandidateService> tempC = new ArrayList<CandidateService>();
		CandidateServices.add(tempC);
		}
	

	//初始化服务流程列表,假定服务活动不超过20个
	for(int i=1;i<=20;i++)
	{
		List<String> tempC = new ArrayList<String>();
		this.Process.add(tempC);
		}

	}


/*此方法用来从文件service.txt中读取所有的候选服务，保存入CandidateServices中*/

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
				tmpCandidate.serviceID=tmpArray[0];  //候选服务编号
								
				tmpCandidate.serviceReliability=Double.parseDouble(tmpArray[2]);  //可靠性
				tmpCandidate.servicePrice=Double.parseDouble(tmpArray[4]);  //价格

				//tmpCandidate.CalculateQ();  
				tmpCandidate.serviceActivityID=(tmpArray[0].split("-"))[0]; //对应的服务活动ID
				
				//将活动ID转换为数字，A=0，B=1...
				char tmpA='A';
				tmpCandidate.serviceActivityNumID=(int)(tmpCandidate.serviceActivityID.charAt(0))-(int)tmpA;
				//将当前的服务保存入指定位置
				CandidateServices.get(tmpCandidate.serviceActivityNumID).add(tmpCandidate);
								
				tmpLine = br.readLine(); //继续读取下一行
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
* 从文件中读取需求，参数代表了第几个需求
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
					tmpLine = br.readLine(); //继续读取下一行
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
* 此函数用来获取流程对
* @param lineNumber 代表了第几个流程
* 
* 流程存储采用的格式为二维list，第1维存储的是每个服务的所有直接子节点；第2维存储的是所有节点的列表
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
						//把节点存储到其父节点对应的列表中
						this.Process.get((int)tmpArray[i].charAt(1)-(int)tmpA).add(tmpNode);
						
						//提取出流程中的节点，记录下来哪些节点出现了，为后续的算法做准备
						if (Nodes.indexOf(tmpArray[i].charAt(1)+"")==-1)  //父节点
							Nodes.add(tmpArray[i].charAt(1)+"");
						if (Nodes.indexOf(tmpArray[i].charAt(3)+"")==-1)  //子节点
							Nodes.add(tmpArray[i].charAt(3)+"");
					
					}
					
					break;
									
				}
				else
				{
					tmpLineNum++;
					tmpLine = br.readLine(); //继续读取下一行
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
* Step1：先处理候选服务集合，根据Nodes中的节点，找到每个节点对应的候选服务集
* 如果为了节省时间，此算法可以融合到getCandidateServices算法中，不过要在getProcess算法之后
* */

public void getNodesCandidateServices()
{   
	//Step1：流程中每个节点对应的候选服务集（注意：本算法假设每个节点在流程中只运行一次）
	//从候选服务全集中，找到出现在流程中节点的所有候选服务集
  for(int i=0;i<this.Nodes.size();i++)
  {
  	List<CandidateService> tmpService=null;
  	char tmpA='A';
  	int tmpIndex=(int)Nodes.get(i).charAt(0)-(int)tmpA;
  	tmpService=this.CandidateServices.get(tmpIndex);
  	this.NodesCandidateServices.add(tmpService);
  }
 
  
  //此段代码用来缩减候选服务的数目，非常重要，用来测试用。  通过调整j的值来控制，当j=500是代表处理所有的。
  List<List<CandidateService>> tmpResult= new ArrayList<List<CandidateService>>();   //保存结果
  for(int i=0;i<this.Nodes.size();i++)
  {
  	List<CandidateService> Result= new ArrayList<CandidateService>();   //保存结果
  	
  	for(int j=0;j<50;j++)
  	{
  		Result.add(this.NodesCandidateServices.get(i).get(j));
  		

  	}
  	tmpResult.add(Result);
  }
	   
  this.NodesCandidateServices=tmpResult;
}


/*
* 进行组合的主函数，采用递归算法
* 
* layer 用来指定当前的层数
* curComposition 用来保存当前的组合片段
*
* 此算法保存所有组合方案
* */

public void compositionAll(int layer, List<CandidateService> curComposition,double curReliability,double curPrice)
{   
	//剩余大于一个集合时：
  if (layer < this.NodesCandidateServices.size() - 1)
  {
    for (int i = 0; i < this.NodesCandidateServices.get(layer).size(); i++)
    {
  	  
  	  //结果的形式应该是一个数组，用来保存每个选择的服务
  	  List<CandidateService> tmpResult= new ArrayList<CandidateService>();  //保存临时结果
        tmpResult.addAll(curComposition);
        tmpResult.add(NodesCandidateServices.get(layer).get(i));
        
        //得到当前组合的QoS值
        double tmpRe = curReliability * NodesCandidateServices.get(layer).get(i).serviceReliability;
        double tmpPr = curPrice + NodesCandidateServices.get(layer).get(i).servicePrice;
        
        //剪枝处理
        if(tmpPr<=this.reqPrice && tmpRe>=this.reqReliability)
      	  compositionAll(layer + 1, tmpResult,tmpRe,tmpPr);
        }
     
  }
  
  //剩余只有一个集合（节点）时：
  else if (layer == this.NodesCandidateServices.size() - 1)
  {
     
          for (int i = 0; i < this.NodesCandidateServices.get(layer).size(); i++)
          {
          	
        	    List<CandidateService> tmpResult= new ArrayList<CandidateService>();  //保存临时结果
              tmpResult.addAll(curComposition);
              tmpResult.add(NodesCandidateServices.get(layer).get(i));
              
              double tmpRe = curReliability * NodesCandidateServices.get(layer).get(i).serviceReliability;
              double tmpPr = curPrice + NodesCandidateServices.get(layer).get(i).servicePrice;
              
              
              if(tmpPr<=this.reqPrice && tmpRe>=this.reqReliability)
              	this.allSolutions.add(tmpResult);
              //此处还可以优化的地方就是判断当前的组合方案目标函数值是否大于当前的最优，大于则保存，否则去掉。
             
          }                
  } 
}





/*
* 进行组合的主函数，采用递归算法
* 
* layer 用来指定当前的层数
* curComposition 用来保存当前的组合片段
*
* 此算法只保存最优
* */

public void compositionBest(int layer, List<CandidateService> curComposition,double curReliability,double curPrice)
{   
	//剩余大于一个集合时：
  if (layer < this.NodesCandidateServices.size() - 1)
  {
    for (int i = 0; i < this.NodesCandidateServices.get(layer).size(); i++)
    {
  	  
  	  //结果的形式应该是一个数组，用来保存每个选择的服务
  	  List<CandidateService> tmpResult= new ArrayList<CandidateService>();  //保存临时结果
        tmpResult.addAll(curComposition);
        tmpResult.add(NodesCandidateServices.get(layer).get(i));
        
        //得到当前组合的QoS值
        double tmpRe = curReliability * NodesCandidateServices.get(layer).get(i).serviceReliability;
        double tmpPr = curPrice + NodesCandidateServices.get(layer).get(i).servicePrice;
        
        //剪枝处理
        if(tmpPr<=this.reqPrice && tmpRe>=this.reqReliability)
      	  compositionBest(layer + 1, tmpResult,tmpRe,tmpPr);
        }
     
  }
  
  //剩余只有一个集合（节点）时：
  else if (layer == this.NodesCandidateServices.size() - 1)
  {
     
          for (int i = 0; i < this.NodesCandidateServices.get(layer).size(); i++)
          {
          	
        	    List<CandidateService> tmpResult= new ArrayList<CandidateService>();  //保存临时结果
              tmpResult.addAll(curComposition);
              tmpResult.add(NodesCandidateServices.get(layer).get(i));
              
              double tmpRe = curReliability * NodesCandidateServices.get(layer).get(i).serviceReliability;
              double tmpPr = curPrice + NodesCandidateServices.get(layer).get(i).servicePrice;
              
             
              if(tmpPr<=this.reqPrice && tmpRe>=this.reqReliability) //判断是否满足约束
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
*运行服务组合算法 
*/

public void runSC(ServiceComposition NewSC)
{   
	//ServiceComposition NewSC=new ServiceComposition();
	
	//此值代表要处理的需求
	int lineNumber=4;
	
	
	
	//从文件中读取所有的候选服务
	NewSC.getAllCandidateServices();  

	//从文件中获取需求，参数代表了第几个需求
	NewSC.getReq(lineNumber);
	System.out.println(NewSC.reqPrice);
	System.out.println(NewSC.reqReliability);
	
	//读取流程
	NewSC.getProcess(lineNumber);
	/*
	for(int i=0;i<NewSC.Nodes.size();i++)  //测试用
	{
		System.out.println(NewSC.Nodes.get(i));
	}
	*/
	
	//读取每个节点的候选服务集合
	NewSC.getNodesCandidateServices();
	/*
	for(int i=0;i<NewSC.NodesCandidateServices.size();i++) //测试用
	{
		for(int j=0;j<NewSC.NodesCandidateServices.get(i).size();j++)
		
			System.out.println(NewSC.NodesCandidateServices.get(i).get(j).serviceID);
	}*/
	
	//调用服务组合算法，找到所有可行的方案
	List<CandidateService> curComposition= new ArrayList<CandidateService>();
	NewSC.compositionBest(0,curComposition,1,0);
	
	NewSC.printBestSolution(NewSC);
		
	
}

/**
*从所有可行服务组合方案中找到最优解 ，配合 compositionAll（）算法一起用
*/

public void findBestSolution(ServiceComposition NewSC)
{   
	//double maxObj=-99999;  //最优目标函数值
 // int optIndex=0;        //最优的服务组合方案序号 
  
	//打印所有组合方案和相应的值
	for(int i=0;i<NewSC.allSolutions.size();i++)
	{   
		double tmpre=1;
		double tmppr=0;
	
		for(int j=0;j<NewSC.allSolutions.get(i).size();j++)
		{
			tmpre=tmpre*NewSC.allSolutions.get(i).get(j).serviceReliability;
			tmppr=tmppr + NewSC.allSolutions.get(i).get(j).servicePrice;
			//System.out.print(NewSC.Result.get(i).get(j).serviceID+"  "); 如果需要打印方案，去掉注释
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
	
	//打印最优方案
	System.out.println("最优解为：");
	for(int j=0;j<NewSC.allSolutions.get(this.ResultIndex).size();j++)
	{
		System.out.print(NewSC.allSolutions.get(this.ResultIndex).get(j).serviceID+"  ");
	}
	
	System.out.println("最优值："+this.ResultObject);
	System.out.println("可靠性："+this.ResultReliability);
	System.out.println("价格：" +this.ResultPrice);
	System.out.println(NewSC.allSolutions.size());
	
}

/*
* 打印出最优值，配合compositionBest()算法使用
* 
*/


public void printBestSolution(ServiceComposition NewSC)
{   
	  

	if (this.ResultObject!=-99999)  //有最优值
	{
		//打印最优方案
		System.out.println("最优解为：");
		for(int j=0;j<this.bestSolution.size();j++)
		{
			System.out.print(this.bestSolution.get(j).serviceID+"  ");
		}
		
		System.out.println("最优值："+this.ResultObject);
		System.out.println("可靠性："+this.ResultReliability);
		System.out.println("价格：" +this.ResultPrice);
				
	}
	else
		System.out.println("没有最优解！");

}


public static void main(String[] args) {

	long beginTime = System.currentTimeMillis();
	ServiceComposition NewSC=new ServiceComposition();
	NewSC.runSC(NewSC);
	
	long endTime = System.currentTimeMillis();
	
	System.out.println("Run time:" +(endTime-beginTime));
	

}

}
