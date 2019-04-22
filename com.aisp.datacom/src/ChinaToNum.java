import java.util.Scanner;

public class ChinaToNum {

    public static void main(String[] args) {
        ChineseToNum obj = new ChineseToNum();
        while (true) {
            System.out.println("选择输入中文数字或者一个句子");
            String string=new Scanner(System.in).nextLine();
            System.out.println(obj.preProcess(string));
        }

    }
}
