package SELabs.Lab1;

/*
* �����洢ÿ����ѡ����
*/
public class CandidateService {
String serviceID=""; //��ѡ����ID
String serviceActivityID=""; //��Ӧ�ķ������ A��B...
int serviceActivityNumID=0; //��Ӧ�ķ������ֱ�� A=0,B=1...
double serviceReliability=0; //����Ŀɿ���
double servicePrice=0; //����۸�
double Objcet=0; //�Ż�Ŀ��
public void CalculateQ()
{
this.Objcet=this.serviceReliability-this.servicePrice/100;
}
public static void main(String[] args) {
// TODO Auto-generated method stub
}
}