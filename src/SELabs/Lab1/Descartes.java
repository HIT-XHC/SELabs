package SELabs.Lab1;

/*
 * 网上找到的一个笛卡尔乘积算法示例
 * */



    import java.util.ArrayList;
    import java.util.List;

    public class Descartes
    {

        public static void run(List<List<String>> dimvalue, List<String> result, int layer, String curstring)
        {
            //大于一个集合时：
            if (layer < dimvalue.size() - 1)
            {
              
                    for (int i = 0; i < dimvalue.get(layer).size(); i++)
                    {
                        StringBuilder s1 = new StringBuilder();
                        s1.append(curstring);
                        s1.append(dimvalue.get(layer).get(i));
                        run(dimvalue, result, layer + 1, s1.toString());
                    }
               
            }
            //只有一个集合时：
            else if (layer == dimvalue.size() - 1)
            {
               
                    for (int i = 0; i < dimvalue.get(layer).size(); i++)
                    {
                        result.add(curstring + dimvalue.get(layer).get(i));
                    }                
            }
        }

        
        /**
         * @param args
         */
        public static void main(String[] args)
        {
            List<List<String>> dimvalue = new ArrayList<List<String>>();
            List<String> v1 = new ArrayList<String>();
            v1.add("a");
            v1.add("b");
            List<String> v2 = new ArrayList<String>();
            v2.add("c");
            v2.add("d");
            v2.add("e");
            List<String> v3 = new ArrayList<String>();
            v3.add("f");
            v3.add("g");
            
            dimvalue.add(v1);
            dimvalue.add(v2);
            dimvalue.add(v3);
            
            List<String> result = new ArrayList<String>();
            
            Descartes.run(dimvalue, result, 0, "");
            
            int i = 1;
            for (String s : result)
            {
                System.out.println(i++ + ":" +s);
            }
        }

    }



