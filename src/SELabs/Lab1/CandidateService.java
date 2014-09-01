package SELabs.Lab1;

/*
* 用来存储每个候选服务
*/
public class CandidateService {
String serviceID=""; //候选服务ID
String serviceActivityID=""; //对应的服务活动编号 A，B...
int serviceActivityNumID=0; //对应的服务活动数字编号 A=0,B=1...
double serviceReliability=0; //服务的可靠性
double servicePrice=0; //服务价格
double Objcet=0; //优化目标
public void CalculateQ()
{
this.Objcet=this.serviceReliability-this.servicePrice/100;
}
public static void main(String[] args) {
// TODO Auto-generated method stub
}
}