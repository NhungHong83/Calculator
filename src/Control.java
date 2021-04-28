
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Control implements ActionListener {

    private Main m;
    private boolean process = false;//Process cu ve true la nhap lai tu dau
    private boolean reset = false;
    private int operate = 0;
    private double NewValue;
    private BigDecimal firstNum;
    private BigDecimal secondNum;
    private BigDecimal memory = new BigDecimal("0");

    public Control(Main m) {
        this.m = m;
        pressButton();
    }

    public void pressButton() {
        m.getBtn0().addActionListener(this);//add action listioner cho button
        m.getBtn1().addActionListener(this);
        m.getBtn2().addActionListener(this);
        m.getBtn3().addActionListener(this);
        m.getBtn4().addActionListener(this);
        m.getBtn5().addActionListener(this);
        m.getBtn6().addActionListener(this);
        m.getBtn7().addActionListener(this);
        m.getBtn8().addActionListener(this);
        m.getBtn9().addActionListener(this);
    }
    //set số lên screen
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();//get text in button
        
        if (process) {//khi click operator thì set screen=0 rồi 
            //process=false để nhập tiếp tục phía sau
            m.txtScreen.setText("0");
            process = false;
            reset = false;
        }
        String txt = m.txtScreen.getText() + cmd;
        BigDecimal number = new BigDecimal(txt);//Ep sang kieu so de cho no khong co so 0 o dau
        //Tinh toan nhieu lan thi kieu double bị hao số
        //Vi sao double bị hao số????
        m.txtScreen.setText(number + "");
    }

    public void calculate(int operate) {
        operator();
        process = true;//Ban đầu = false => Nhập tiếp, khi click phép toán +,-,*,/
        //Thi chuyển thành true để nhập lại từ đầu
        this.operate = operate;//để biết ng dùng bấm phép tính gì
    }

    public BigDecimal getValue() {
        return new BigDecimal(m.txtScreen.getText());
    }
    //test case 1 
    //process=false=> bấm số ()=>bấm Operator( get firstNum, process=true, lưu operator)
    //=>Bấm operator ( Không thực hiện vì process=true )
    //Bấm số ( process=false )
    //=> Bấm operator (get secondNum,Thực hiện phép tính, result= firstNum, process=true)
    //Kết luận:
    //=> Cứ bấm operator thì cho process= true để không bấm được operator lần nữa
    //=> Cứ bấm số thì set process = false để được bấm operator
    
    
    public void operator() {
        if (!process) {
            if (operate == 0) {
                firstNum = getValue();             
            } else {
                secondNum = getValue();
                switch (operate) {
                    case 1://add
                        firstNum = firstNum.add(secondNum);
                        break;
                    case 2://sub
                        firstNum = firstNum.subtract(secondNum);
                        break;
                    case 3://multi
                        firstNum = firstNum.multiply(secondNum);
                        break;
                    case 4://devi
                        if (secondNum.doubleValue() == 0) {
                            m.txtScreen.setText("ERROR");
                            return;
                        }
                        firstNum = firstNum.divide(secondNum, 20, RoundingMode.HALF_UP).stripTrailingZeros();
                        firstNum = new BigDecimal(firstNum.toPlainString());
                         //NewValue = Double.parseDouble(firstNum+"");
                        break;
                }
                m.txtScreen.setText(firstNum + "");
            }
        }
    }
//////////////////////////////
    public void pressResult() {
        operator();
        process = true;// để khi press "=" thì nhập lại số mới, nếu k có thì số sẽ nhập tiếp phía sau dấu "="
        operate = 0;// Để tính toán lại từ đấu? Tại sao???
    }
    //Test case 2: 5-=+3+
    //process=false=> bấm số (5)=>bấm Operator "-"( get firstNum=5, process=true, lưu operate="-")
    //Bấm "=" (Hàm operator không thực hiện vì process=true, operate=0)
    //Bấm "+" (Không thực hiện vì process =true, operate= "+" )
    //Bấm số (3) process=false
    //2.1. Bấm "+" (Lưu secondNum=3; process=true, operate="+", kqua=8, firstNum=8;
        //Bấm "=" (process=true, operate="0")=> Bấm dấu thì set Operator tiếp
        //Bấm 2
    //Sau khi bấm dấu thì process=true và set Operate
    //Riêng bấm "=" thì process=true và operate=0
    
    public void pressPercen() {
//        double value = getValue().doubleValue() / 100;
//// Lấy gtri trên màn hình ép sang kiểu double rồi chia cho 100
//        m.txtScreen.setText(value + "");
//bo sung:
m.txtScreen.setText(BigDecimal.valueOf(getValue().doubleValue()/100).stripTrailingZeros().toPlainString());
        firstNum = getValue();
        process = true;
        
        reset = true;
    }

    public void pressClear() {
        process = false;
        operate = 0;
        firstNum = new BigDecimal("0");
        secondNum = new BigDecimal("0");
        m.txtScreen.setText("0");
    }

    public void pressInvert() {
        double value = getValue().doubleValue();
        double rs = 0;
        if (value != 0) {
            rs = 1 / value;
            m.txtScreen.setText(new BigDecimal(rs)+"");
            firstNum = getValue();
        } else {
            m.txtScreen.setText("ERROR");
        }
        process = true;
        reset = true;
    }

    public void pressSqrt() {
        double value = getValue().doubleValue();
        double rs = 0;
        if (value >= 0) {
            rs = Math.sqrt(value);
            m.txtScreen.setText(rs + "");
            firstNum = getValue();
        } else {
            m.txtScreen.setText("ERROR");
        }
        process = true;
        reset = true;
    }

    public void pressSwap() {
        String txt = m.txtScreen.getText();
        if(txt.equals("ERROR")){
            return;
        }
        if(txt.startsWith("-")){
            txt = txt.substring(1);
        }else{
            txt = "-"+txt;
        }
        m.txtScreen.setText(txt);
    }

    public void pressDot() {
        String txt = m.txtScreen.getText();
        if (!txt.contains(".")) {
            txt += ".";
        }
        NewValue=Double.parseDouble(txt);
        m.txtScreen.setText(NewValue+"");
    }

    public void pressMAdd() {
        memory = memory.add(getValue());
        process = true;
    }

    public void pressMSub() {
        memory = memory.subtract(getValue());
         process = true;
    }

    public void pressMR() {
        m.txtScreen.setText(memory + "");
    }

    public void pressMClear() {
        memory = new BigDecimal("0");
    }
    
    public void removeLast(){
        String txt = m.txtScreen.getText();
        if(txt.length()==2 && txt.startsWith("-")){
            m.txtScreen.setText("0");
            return;
        }
        if(txt.length()==1){
            m.txtScreen.setText("0");
            return;
        }
        txt = txt.substring(0, txt.length()-1);
        m.txtScreen.setText(txt);
    }

}
