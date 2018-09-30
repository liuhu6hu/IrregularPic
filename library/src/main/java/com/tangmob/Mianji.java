package com.tangmob;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  print(unicode("请逆时针点击图中n点，划取区域。", encoding="utf-8"))
 pos=plt.ginput(nn)
 print(unicode("你选择的n个点坐标为:", encoding="utf-8"))
 print(pos)
 # 计算选点面积和比例
 pos.append(pos[0])
 S_clicked = 0
 for k in range(0,nn):
    S_clicked = S_clicked+pos[k][0]*pos[k+1][1]-pos[k+1][0]*pos[k][1]
    S_clicked = abs(0.5*S_clicked)
    print(unicode("你选择的区域面积为:", encoding="utf-8"))
    print(S_clicked)
 scale = S_clicked/S_picture
 #f = int(input('请输入f(mm):'))
 f = 11
 H = int(input('请输入拍摄高度(m):'))
 # 定义占地面积计算函数
 def Rarea(f,H):
     RS = Sccd[0]*Sccd[1]*((H*1000)**2)/(f**2)
     RS_clicked = RS * scale
     return RS_clicked
 # 输出结果
 RealS = Rarea(f,H)/1000000
 RRealS = RealS+(aa*H-bb)
 print(unicode("你选取区域的实际占地面积（㎡）为：", encoding="utf-8"))
 print(RRealS)
 jixu = int(input('是否继续选点,输入数字1：继续    输入数字2：结束    :'))
 if jixu == 2:
 break
 */
public class Mianji {
    private static double[] sccd = {12.8,9.6};
    private static double aa = 0.0889;
    private static double bb = -0.8889;
    private static double[] S = {14.4,9.6} ;
    private double height=0;
    private static double f=0; //光圈 ,芯片到镜头的距离
    private static double C=0;


    private static void initGuige(String gui){
        if(gui.equals("1/2.3")) {
            S[0]=6.16;
            S[1] =4.62;

        }else if(gui.equals("3/3")) {
           S[0]=12.7;
           S[1] =9.6;

        }
        else if(gui.equals("4/3")){
          S[0]=17.8;
          S[1]=13.4;

        }
        //4.59*3.42
        else if(gui.equals("1/2.8")){
          S[0]=4.59;
          S[1]=3.42;

        }
       else{
           S[0]=14.4;
           S[1]=9.6;

        }

    }

    private static void adjustSccd(double imgWidth,double imgHeight){
        double b = imgWidth/imgHeight;
        System.out.println("imgWidth("+imgWidth+")/imgHeight("+imgHeight+")=="+(int)b);

        if((int)b==4/3){
//            sccd[0] = 12.8;
//            sccd[1]=9.6;
        }else if((int)b==16/9){
            System.out.println("imgHeight/imgWidth:"+(int)b+"======16/9:"+16/9);
//            sccd[0]=14.4;
//            sccd[1]=8.1;
            S[1]=S[1]*(3/4.0);
        }else{
//            sccd[0]=b*9.6;
            S[1]=imgWidth/imgHeight*S[0];
            System.out.println("该图片尺寸异常，计算结果可能有误。");
        }
//        if b == 4/3:
//        S[0] = 12.8
//        elif b == 16/9:
//        S[1] = 8.1
//    else:
//        Sccd[0] = a[1]/a[0]*9.6
//        print(unicode("该图片尺寸异常，计算结果可能有误。", encoding="utf-8"))
//        return S
    }
    /**
     * @param cmos cmos规格 1/2.3,3/3,4/3
     * @param imgWidth 图像宽度
     * @param imgHeight 图像高度
     * @param height 航拍高度
     * @param fav 航拍角度
     * @param posList 坐标列表,[Pos<x,y>,Pos<x,y>...]
     * @throws Exception
     * @return 计算后的面积，单位是平方米
     */
    public static double cal(String cmos,
                             double imgWidth,
                             double imgHeight,
                             double height,
                             double fav,List<Pos> posList) throws Exception {

        if(!cmos.equals("4/3") && !cmos.equals("3/3") && !cmos.equals("1/2.3") && !cmos.equals("1/2.8"))
            throw new Exception("CMOS选择错误");
        if(posList==null || posList.size()<3)
            throw new Exception("坐标不能为空,并且不能少于3个");


        initGuige(cmos);
//        System.out.println(cmosPoint);
//        C =  Math.sqrt(Math.pow(14.4,2)+Math.pow(9.6,2));
        C =  Math.sqrt(Math.pow(S[0],2)+Math.pow(S[1],2));
        double  S_picture = imgHeight *imgWidth;
        System.out.println("全图片区域像素面积为:"+S_picture);
        double S_clicked=0;
        adjustSccd(imgWidth,imgHeight);
        System.out.println("sccd[0]:"+S[0]+"----sccd[1]:"+S[1]);
        posList.add(posList.get(0));
        System.out.println("size:"+posList.size());
        for (int i=0;i<posList.size();i++){
            if(i==posList.size()-1){
                break;
            }
            Pos pos = posList.get(i);
            Pos pos1 = posList.get(i+1);
//            System.out.println(i+"---"+pos+"-----"+pos1);
            S_clicked = S_clicked+ pos.x * pos1.y - pos1.x * pos.y;
        }
        S_clicked = Math.abs(0.5 * S_clicked);

//        Pos posLast = posList.get(posList.size()-1);
//        S_clicked = S_clicked+ posLast.x * posLast.y;
//        S_clicked = Math.abs(0.5 * S_clicked);


        System.out.println("区域面积:"+S_clicked);
        double scale = S_clicked/S_picture;
        System.out.println("scale:"+scale);
        f = Math.abs(fav);
//       double RealS =  rarea(f,height,C,scale)/1000000;
       double RealS_clicked =  rarea(f,height,C,scale);
       if(f==63.7){
           RealS_clicked = a637(height,scale);
       }
        System.out.println("RealS_clicked:"+RealS_clicked);
//       double RRealS = RealS+(aa*height-bb);
        double RRealS = RealS_clicked;
//        if(fav==72)
//            RRealS = RRealS-7;
        System.out.println("你选取区域的实际占地面积（㎡）为："+RRealS);
        return RRealS;
    }

    private static double rarea(double f,double H,double C,double scale) {
//        double rs = sccd[0]*sccd[1]*((H*1000)**2)/(f**2);
        //  F = (C/2)/math.tan(float(f/2)/180*math.pi)
        System.out.println("f:"+f+",H:"+H+",C:"+C+",scale:"+scale+",sccd[0]:"+S[0]+",sccd[1]:"+S[1]);
        double F = (C/2)/Math.tan((f/2)/180*Math.PI);
        double Real_C = C*H/F;
        double RL = Real_C*S[0]/C;
        double RW = Real_C*S[1]/C;
        double RS_tpicture = RL*RW;
        double RS_clicked = RS_tpicture*scale;
//        double rs = sccd[0]*sccd[1]*(Math.pow((H*1000),2))/(Math.pow(f,2));
//        double RS_clicked = rs * scale;
        return RS_clicked;
    }

    private static double a637(double H,double scale){
        double RRealS = (Math.pow(H,4.12)*0.0002075+83.49)*scale;
        return RRealS;

    }


    public static void main(String[] args) throws Exception {
        //33.49582468245026 平方米
        Pos p1 = new Pos(676.9193548387098,202.72580645161293);
        Pos p2 = new Pos(1091.1129032258066,175.62903225806463);
        Pos p3 = new Pos(1075.6290322580646, 647.8870967741935);
        Pos p4 = new Pos(688.5322580645163,674.983870967742);
//        Pos p5 = new Pos(344.6612903225806,398.04838709677415);
//        Pos p6 = new Pos(181.758064516129,389.9838709677419);
        List<Pos> lst = new ArrayList();
        lst.add(p1);
        lst.add(p2);
        lst.add(p3);
        lst.add(p4);
//        lst.add(p5);
//        lst.add(p6);
         Mianji.cal("1/2.8",1080,1920,30,63.7,lst);
    }
}
