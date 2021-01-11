package cn.edu.aurora.util;

import com.mongodb.*;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.BytesWritable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class ImageUtil {


    @Autowired
    public MongoTemplate mongoTemplate;

    public static void handleImage(byte[] imageBytes, String ufileName) throws IOException {

        //*************************************预处理**************************************
        BytesWritable value = new BytesWritable(imageBytes);
        String date="";
        String time="";
        String hour="";
        String waveband="";

        time = ufileName.substring(1, 7) + "123451";

        /*try {
            DataInputStream dis=new DataInputStream(new ByteArrayInputStream(imageBytes));
            InputStreamReader rea=new InputStreamReader(dis);
            BufferedReader br = new BufferedReader(rea);

            String read = br.readLine();
            System.out.println(read);


            br.skip(3*read.length());
            int index_comment=read.indexOf("[Comment]");

            int index = read.indexOf("Date");
            date = read.substring(index+6,index+10)+read.substring(index+11,index+13)+read.substring(index+14,index+16);

            if(index_comment==-1)
            {
                int index1 = read.indexOf("Time");
                time = read.substring(index1+6,index1+8)+read.substring(index1+9,index1+11)+read.substring(index1+12,index1+14);
                hour = read.substring(index1+6,index1+8);
            }
            else
            {
                time=read.substring(index_comment+34,index_comment+36)+read.substring(index_comment+37,index_comment+39)+ read.substring(index_comment+40,index_comment+42);
                hour= read.substring(index_comment+34,index_comment+36);
            }
            br.close();
        }
        catch (IOException ie){
            System.err.println(ie.getMessage());
        }*/

        //------------------------------------------------------------------------------------------------
        int noiseR=1137,noiseG=546,noiseV=594,LimRayleighR=4000,LimRayleighG=4000,LimRayleighV=4000,Rx0=256,Ry0=257,Gx0=261,Gy0=257,Vx0=257,Vy0=255;
        float RK= (float) 0.5159,GK=(float) 1.0909,VK=(float) 1.5280,rotatedegree=(float) -61.1;
        String savetype=".jpg";

        float I,K=0.0f;
        String fileName;
        int noise=0,LimRayleigh=1,x0=0,y0=0;
        int r=220;

        BufferedImage bi = new BufferedImage(512, 512,BufferedImage.TYPE_INT_RGB);   //在内存中生成512x512的图像缓冲区，TYPE_INT_RGB表示一个图像，该图像具有整数像素的 8 位 RGB颜色

        Graphics g = bi.getGraphics();

        fileName=ufileName;    //获取文件名
        //fileName=fileName.substring(fileName.lastIndexOf("/")+1);

        System.out.println(fileName);

        g.clearRect(0, 0, 2*r, 2*r);   //清除出一个440x440的矩形区域

        if(fileName.charAt(0) == 'E' && fileName.charAt(1) == 'N' )
        {
            if (fileName.charAt(8) == 'R')
            {   //判断文件属于哪一个波段

                noise = noiseR;
                LimRayleigh = LimRayleighR;
                x0=Rx0;
                y0=Ry0;
                K = RK;
            }
            else if (fileName.charAt(8) == 'G')
            {
                noise = noiseG;
                LimRayleigh = LimRayleighG;
                x0=Gx0;
                y0=Gy0;
                K = GK;
            }
            else if (fileName.charAt(8) == 'V')
            {
                noise = noiseV;
                LimRayleigh =LimRayleighV;
                x0=Vx0;
                y0=Vy0;
                K = VK;
            }
        }
        else{
            if (fileName.charAt(7) == 'R')
            {   //判断文件属于哪一个波段
                noise = noiseR;
                LimRayleigh = LimRayleighR;
                x0=Rx0;
                y0=Ry0;
                K = RK;
            }
            else if (fileName.charAt(7) == 'G')
            {
                noise = noiseG;
                LimRayleigh = LimRayleighG;
                x0=Gx0;
                y0=Gy0;
                K = GK;
            }
            else if (fileName.charAt(7) == 'V')
            {
                noise = noiseV;
                LimRayleigh =LimRayleighV;
                x0=Vx0;
                y0=Vy0;
                K = VK;
            }
        }

        try
        {
            int result[][];

            result = new int[512][512];
            int point =value.getLength()-524289;

            for(int i=0;i<512;i++)
                for(int j=0;j<512;j++)
                {
                    result[i][j]=getByte(imageBytes[point+1],imageBytes[point+2]);
                    point=point+2;
                }

            for (int i= 0; i <= 511; i++){

                for (int j = 0; j <= 511; j++)
                {
                    int p =result[j][i];  //进行读出文件字节的转秩
                    I =(p-noise)*K;
                    I = (I/LimRayleigh);
                    if(Math.pow((j-y0),2)+Math.pow((i-x0),2)>=Math.pow(r,2)) //半径r圆形区域外的区域填充黑色
                        I=0;
                    else
                    {if(I<0)
                        I=0;
                        if(I>1)
                            I=1;
                    }
                    g.setColor(new Color(I, I, I));   //此处为选则画点的颜色，R=G=B=0为黑色。R=G=B=1为白色
                    g.drawLine(i,j , i, j);    //在（i,j）处以上面的颜色画点

                }
            }
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        BufferedImage img =new BufferedImage(512 ,512,BufferedImage.TYPE_INT_RGB); //对图像进行-61.1度的逆时针旋转
        BufferedImage img1 =new BufferedImage(440 ,440,BufferedImage.TYPE_INT_RGB);
        AffineTransform transform = new AffineTransform ();
        transform.rotate(rotatedegree*Math.PI/180,x0,y0);

        AffineTransformOp op = new AffineTransformOp(transform,null);
        op.filter(bi, img);
        img1=img.getSubimage( x0-r,  y0-r,  440,  440);

        bi.flush();
        img.flush();

        ImageIO.write(img1, "jpg", new File("D:\\pretest.jpg"));
        //*******************************************************************************

        //*************************************生成缩略图***********************************
        BufferedImage target = null;
        int targetW=75;
        int targetH=75;
        BufferedImage source=img1;
        int type = source.getType();
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        if(sx>sy)
        {
            sx = sy;
            targetW = (int)(sx * source.getWidth());
        }
        else
        {
            sy = sx;
            targetH = (int)(sy * source.getHeight());
        }
        if (type == BufferedImage.TYPE_CUSTOM) { //handmade
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        }
        else
            target = new BufferedImage(targetW, targetH, type);

        Graphics2D gg = target.createGraphics();

        //smoother than exlax:
        gg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
        gg.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        gg.dispose();
        File saveFile = new File("D:/mintest.jpg");
        ImageIO.write(target,"jpg",saveFile);
        //******************************************************************************

        //*******************************提取keogram条列***********************************
        Object keoB[] = new Object[440];
        for(int i=0;i<440;i++) {
            keoB[i] = img1.getRGB(221, i);
        }
        //******************************************************************************

        //**************************************生成数据库需要保存的信息*************************
        //name:ufilename,time:time,band:band
        //original
        //thumb
        //keogram:keoB[i]
        //feature:无

        Mongo mg = new Mongo();

        DB db = mg.getDB("aurora");

        DBCollection meta = db.getCollection("Aurora.Meta");
        DBCollection original = db.getCollection("Aurora.Image");
        DBCollection thumb=db.getCollection("Aurora.Thumb");
        DBCollection keogram=db.getCollection("Aurora.Keogram");
        DBCollection feature=db.getCollection("Aurora.Feature");

        ufileName = ufileName.substring(0,13);
        time = date+time;
        waveband = ""+ufileName.charAt(7);

        BasicDBObject metadbo = new BasicDBObject();
        metadbo.put("name",ufileName);
        metadbo.put("time", time);
        metadbo.put("band",waveband);
        metadbo.put("manualtype", "0");
        meta.insert(metadbo);

        BasicDBObject picdbo = new BasicDBObject();
        ByteArrayOutputStream oribos = new ByteArrayOutputStream();      //字节输出流
        ImageIO.write(img1, "bmp", oribos);      //将虚拟图片信息写入到字节输出流中
        byte[] orib = oribos.toByteArray();//generate byte[]     //输出流将字节信息输出到byte数组b中
        picdbo.put("name",ufileName);
        picdbo.put("rawpic",orib);
        original.insert(picdbo);


        BasicDBObject thumbdbo = new BasicDBObject();
        ByteArrayOutputStream thumbbos = new ByteArrayOutputStream();      //字节输出流
        ImageIO.write(target, "bmp", thumbbos);      //将虚拟图片信息写入到字节输出流中
        byte[] thumbb = thumbbos.toByteArray();//generate byte[]     //输出流将字节信息输出到byte数组b中
        thumbdbo.put("name",ufileName);
        thumbdbo.put("thumb",thumbb);
        thumb.insert(thumbdbo);

        BasicDBObject keogramdbo = new BasicDBObject();
        keogramdbo.put("name",ufileName);
        System.out.println(keoB);
        keogramdbo.put("keogram",keoB);
        keogram.insert(keogramdbo);

        BasicDBObject featuredbo = new BasicDBObject();
        featuredbo.put("name",ufileName);
        featuredbo.put("feature",ImageUtil.produceFingerPrint(orib));
        feature.insert(featuredbo);
    }

    public static int getByte(byte a,byte b) {
        return (int) ((b & 0xFF)*256+ (a & 0xFF));
    }

    /**
     * 生成缩略图 <br/>
     * 保存:ImageIO.write(BufferedImage, imgType[jpg/png/...], File);
     *
     * @param source
     *            原图片
     * @param width
     *            缩略图宽
     * @param height
     *            缩略图高
     * @param b
     *            是否等比缩放
     * */
    public static BufferedImage thumb(BufferedImage source, int width,
                                      int height, boolean b) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target = null;
        double sx = (double) width / source.getWidth();
        double sy = (double) height / source.getHeight();

        if (b) {
            if (sx > sy) {
                sx = sy;
                width = (int) (sx * source.getWidth());
            } else {
                sy = sx;
                height = (int) (sy * source.getHeight());
            }
        }

        if (type == BufferedImage.TYPE_CUSTOM) { // handmade
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width,
                    height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else
            target = new BufferedImage(width, height, type);
        Graphics2D g = target.createGraphics();
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    /**
     * 图片水印
     *
     * @param imgPath
     *            待处理图片
     * @param markPath
     *            水印图片
     * @param x
     *            水印位于图片左上角的 x 坐标值
     * @param y
     *            水印位于图片左上角的 y 坐标值
     * @param alpha
     *            水印透明度 0.1f ~ 1.0f
     * */
    public static void waterMark(String imgPath, String markPath, int x, int y,
                                 float alpha) {
        try {
            // 加载待处理图片文件
            Image img = ImageIO.read(new File(imgPath));

            BufferedImage image = new BufferedImage(img.getWidth(null),
                    img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(img, 0, 0, null);

            // 加载水印图片文件
            Image src_biao = ImageIO.read(new File(markPath));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.drawImage(src_biao, x, y, null);
            g.dispose();

            // 保存处理后的文件
            FileOutputStream out = new FileOutputStream(imgPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文字水印
     *
     * @param imgPath
     *            待处理图片
     * @param text
     *            水印文字
     * @param font
     *            水印字体信息
     * @param color
     *            水印字体颜色
     * @param x
     *            水印位于图片左上角的 x 坐标值
     * @param y
     *            水印位于图片左上角的 y 坐标值
     * @param alpha
     *            水印透明度 0.1f ~ 1.0f
     */

    public static void textMark(String imgPath, String text, Font font,
                                Color color, int x, int y, float alpha) {
        try {
            Font Dfont = (font == null) ? new Font("宋体", 20, 13) : font;

            Image img = ImageIO.read(new File(imgPath));

            BufferedImage image = new BufferedImage(img.getWidth(null),
                    img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            g.drawImage(img, 0, 0, null);
            g.setColor(color);
            g.setFont(Dfont);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.drawString(text, x, y);
            g.dispose();
            FileOutputStream out = new FileOutputStream(imgPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 读取JPEG图片
     * @param filename 文件名
     * @return BufferedImage 图片对象
     */
    public static BufferedImage readJPEGImage(String filename)
    {
        try {
            InputStream imageIn = new FileInputStream(new File(filename));
            // 得到输入的编码器，将文件流进行jpg格式编码
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
            // 得到编码后的图片对象
            BufferedImage sourceImage = decoder.decodeAsBufferedImage();

            return sourceImage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ImageFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 读取JPEG图片
     * @param filename 文件名
     * @return BufferedImage 图片对象
     */
    public static BufferedImage readPNGImage(String filename)
    {
        try {
            File inputFile = new File(filename);
            BufferedImage sourceImage = ImageIO.read(inputFile);
            return sourceImage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ImageFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 灰度值计算
     * @param pixels 像素
     * @return int 灰度值
     */
    public static int rgbToGray(int pixels) {
        // int _alpha = (pixels >> 24) & 0xFF;
        int _red = (pixels >> 16) & 0xFF;
        int _green = (pixels >> 8) & 0xFF;
        int _blue = (pixels) & 0xFF;
        return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);
    }

    /**
     * 计算数组的平均值
     * @param pixels 数组
     * @return int 平均值
     */
    public static int average(int[] pixels) {
        float m = 0;
        for (int i = 0; i < pixels.length; ++i) {
            m += pixels[i];
        }
        m = m / pixels.length;
        return (int) m;
    }



    /**
     * 计算"汉明距离"（Hamming distance）。
     * 如果不相同的数据位不超过5，就说明两张图片很相似；如果大于10，就说明这是两张不同的图片。
     * @param sourceHashCode 源hashCode
     * @param hashCode 与之比较的hashCode
     */
    public static int hammingDistance(String sourceHashCode, String hashCode) {
        int difference = 0;
        int len = sourceHashCode.length();

        for (int i = 0; i < len; i++) {
            if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
                difference ++;
            }
        }

        return difference;
    }

    /**
     * 生成图片指纹，即 feature，用于判断相似度
     * @param b
     * @return
     * @throws IOException
     */
    public static String produceFingerPrint(byte[] b) throws IOException {

        ByteArrayInputStream in = new ByteArrayInputStream(b);
        BufferedImage source = ImageIO.read(in);

        int width = 8;
        int height = 8;

        // 第一步，缩小尺寸。
        // 将图片缩小到8x8的尺寸，总共64个像素。这一步的作用是去除图片的细节，只保留结构、明暗等基本信息，摒弃不同尺寸、比例带来的图片差异。
        BufferedImage thumb = ImageUtil.thumb(source, width, height, false);

        // 第二步，简化色彩。
        // 将缩小后的图片，转为64级灰度。也就是说，所有像素点总共只有64种颜色。
        int[] pixels = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i * height + j] = ImageUtil.rgbToGray(thumb.getRGB(i, j));
            }
        }

        // 第三步，计算平均值。
        // 计算所有64个像素的灰度平均值。
        int avgPixel = ImageUtil.average(pixels);

        // 第四步，比较像素的灰度。
        // 将每个像素的灰度，与平均值进行比较。大于或等于平均值，记为1；小于平均值，记为0。
        int[] comps = new int[width * height];
        for (int i = 0; i < comps.length; i++) {
            if (pixels[i] >= avgPixel) {
                comps[i] = 1;
            } else {
                comps[i] = 0;
            }
        }

        // 第五步，计算哈希值。
        // 将上一步的比较结果，组合在一起，就构成了一个64位的整数，这就是这张图片的指纹。组合的次序并不重要，只要保证所有图片都采用同样次序就行了。
        StringBuffer hashCode = new StringBuffer();
        for (int i = 0; i < comps.length; i+= 4) {
            int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2) + comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 2];
            hashCode.append(binaryToHex(result));
        }

        // 得到指纹以后，就可以对比不同的图片，看看64位中有多少位是不一样的。
        return hashCode.toString();
    }

    /**
     * 生成图片指纹，用于判断相似度
     * @param filename 文件名，用于生成静态资源文件夹中已经存在的文件的 feature
     * @return 图片指纹
     */
    public static String produceFingerPrint(String filename) {
        BufferedImage source = ImageUtil.readPNGImage(filename);// 读取文件

        int width = 8;
        int height = 8;

        // 第一步，缩小尺寸。
        // 将图片缩小到8x8的尺寸，总共64个像素。这一步的作用是去除图片的细节，只保留结构、明暗等基本信息，摒弃不同尺寸、比例带来的图片差异。
        BufferedImage thumb = ImageUtil.thumb(source, width, height, false);

        // 第二步，简化色彩。
        // 将缩小后的图片，转为64级灰度。也就是说，所有像素点总共只有64种颜色。
        int[] pixels = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i * height + j] = ImageUtil.rgbToGray(thumb.getRGB(i, j));
            }
        }

        // 第三步，计算平均值。
        // 计算所有64个像素的灰度平均值。
        int avgPixel = ImageUtil.average(pixels);

        // 第四步，比较像素的灰度。
        // 将每个像素的灰度，与平均值进行比较。大于或等于平均值，记为1；小于平均值，记为0。
        int[] comps = new int[width * height];
        for (int i = 0; i < comps.length; i++) {
            if (pixels[i] >= avgPixel) {
                comps[i] = 1;
            } else {
                comps[i] = 0;
            }
        }

        // 第五步，计算哈希值。
        // 将上一步的比较结果，组合在一起，就构成了一个64位的整数，这就是这张图片的指纹。组合的次序并不重要，只要保证所有图片都采用同样次序就行了。
        StringBuffer hashCode = new StringBuffer();
        for (int i = 0; i < comps.length; i+= 4) {
            int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2) + comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 2];
            hashCode.append(binaryToHex(result));
        }

        // 得到指纹以后，就可以对比不同的图片，看看64位中有多少位是不一样的。
        return hashCode.toString();
    }

    /**
     * 二进制转为十六进制
     * @return char hex
     */
    private static char binaryToHex(int binary) {
        char ch = ' ';
        switch (binary)
        {
            case 0:
                ch = '0';
                break;
            case 1:
                ch = '1';
                break;
            case 2:
                ch = '2';
                break;
            case 3:
                ch = '3';
                break;
            case 4:
                ch = '4';
                break;
            case 5:
                ch = '5';
                break;
            case 6:
                ch = '6';
                break;
            case 7:
                ch = '7';
                break;
            case 8:
                ch = '8';
                break;
            case 9:
                ch = '9';
                break;
            case 10:
                ch = 'a';
                break;
            case 11:
                ch = 'b';
                break;
            case 12:
                ch = 'c';
                break;
            case 13:
                ch = 'd';
                break;
            case 14:
                ch = 'e';
                break;
            case 15:
                ch = 'f';
                break;
            default:
                ch = ' ';
        }
        return ch;
    }

    public static void main(String[] args) {
        List<String> hashCodes = new ArrayList<String>();

        String f= "D:\\Pan\\IDEAWorkspace\\AuroraManagement\\src\\main\\resources\\img\\";
        File file = new File( f);

        if(file.exists())
        {
            File[] filelist = file.listFiles();
            for(int i = 0;i<filelist.length;i++)
            {
                String filename = filelist[i].getName();
                String hashCode = null;
                hashCode = produceFingerPrint(f+filename);
                hashCodes.add(hashCode);
            }
        }

        System.out.println("Resources: ");
        System.out.println(hashCodes);
        System.out.println();

        String sourceHashCode = produceFingerPrint(f+"N031221G00001.jpg");
        System.out.println("Source: ");
        System.out.println(sourceHashCode);
        System.out.println();
        //计算一张图片跟一系列图片的汉明距离
        List<Integer> differences = new ArrayList<Integer>();
        for (int i = 0; i < hashCodes.size(); i++)
        {
            int difference = hammingDistance(sourceHashCode, hashCodes.get(i));
            differences.add(difference);
        }

        System.out.println(differences);
        //计算某一张图片跟另一张图片的相似性
        String hashCode1 = "3c7fcf0f0f0f0f3c";
        int difference = hammingDistance(sourceHashCode, hashCode1);

        System.out.println("Hanming Distance between src: " + sourceHashCode + " and dest: " + hashCode1 + " is: " + difference);
        if (difference <= 5){
            System.out.println("Match");
        } else if (difference <= 10) {
            System.out.println("Like");
        } else {
            System.out.println("Not Match");
        }


    }
}
