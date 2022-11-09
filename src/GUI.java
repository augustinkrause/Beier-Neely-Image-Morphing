
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class GUI {

    public JFrame frame;

    public DrawableLabel leftImage;
    public DrawableLabel rightImage;
    public JPanel leftImageContainer;
    public String leftImagePath = "/Users/Augustin/Desktop/augustin/programmieren/video/beier-neely/src/bild1.png";
    public Image bLeftImage;

    public JPanel rightImageContainer;
    public JButton leftImageBrowse;
    public JButton rightImageBrowse;
    public String rightImagePath = "/Users/Augustin/Desktop/augustin/programmieren/video/beier-neely/src/bild2.png";
    public Image bRightImage;

    public JButton morphButton;
    public JButton clearButton;
    public JButton undoButton;

    private Stack<GUIState> states = new Stack<GUIState>();

    public GUI() throws IOException {

        frame = new JFrame();
        frame.setSize(750, 750);
        frame.setTitle("Beierneely");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1;
        gc.weighty = 1;

        leftImageContainer = new JPanel();
        leftImageContainer.setLayout(new GridBagLayout());
        GridBagConstraints gcLeft = new GridBagConstraints();
        gcLeft.weightx = 1;
        gcLeft.weighty = 1;
        leftImage = new DrawableLabel();
        leftImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                ArrayList<Feature> tempF = (ArrayList<Feature>) leftImage.feats.clone();
                tempF.remove(tempF.size() - 1);
                states.push(new GUIState(tempF, (ArrayList<Feature>) rightImage.feats.clone(), (Stack<Feature>) leftImage.fStack.clone(),(Stack<Feature>) rightImage.fStack.clone()));
                super.mouseReleased(e);
                if(leftImage.feats.size() + leftImage.fStack.size() > rightImage.feats.size() + rightImage.fStack.size()){
                    Feature f = leftImage.feats.get(leftImage.feats.size() - 1);
                    leftImage.feats.remove(leftImage.feats.size() - 1);
                    leftImage.fStack.push(f);
                    leftImage.repaint();
                }else{
                    Feature f = rightImage.fStack.pop();
                    rightImage.feats.add(f);
                    rightImage.repaint();
                    leftImage.repaint();
                }
                if(leftImage.feats.size() == rightImage.feats.size() && leftImage.fStack.isEmpty() && rightImage.fStack.isEmpty()){
                    morphButton.setEnabled(true);
                }else{
                    morphButton.setEnabled(false);
                }
                undoButton.setEnabled(true);
            }

        });
        BufferedImage i = javax.imageio.ImageIO.read(new File(this.leftImagePath));
        Image i2 = i.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        this.bLeftImage = (i.getWidth() > CONSTANT.MAX_WIDTH || i.getHeight() > CONSTANT.MAX_HEIGHT)? this.resize(i,CONSTANT.MAX_WIDTH, (int) Math.round(((double)CONSTANT.MAX_WIDTH / i.getWidth()) * i.getHeight())): i;
        leftImage.setIcon(new ImageIcon(i2));
        leftImage.setHorizontalAlignment(JLabel.CENTER);
        gcLeft.gridx = 0;
        gcLeft.gridy = 0;
        leftImageContainer.add(leftImage, gcLeft);
        leftImageBrowse = new JButton("Browse");
        leftImageBrowse.setHorizontalAlignment(JLabel.CENTER);
        leftImageBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                leftBrowseButtonActionPerformed(e);
            }
        });
        gcLeft.gridx = 0;
        gcLeft.gridy = 1;
        leftImageContainer.add(leftImageBrowse, gcLeft);

        rightImageContainer = new JPanel();
        rightImageContainer.setLayout(new GridBagLayout());
        GridBagConstraints gcRight = new GridBagConstraints();
        gcRight.weightx = 1;
        gcRight.weighty = 1;
        rightImage = new DrawableLabel();
        rightImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                ArrayList<Feature> tempF = (ArrayList<Feature>) rightImage.feats.clone();
                tempF.remove(tempF.size() - 1);
                states.push(new GUIState((ArrayList<Feature>) leftImage.feats.clone(), tempF, (Stack<Feature>) leftImage.fStack.clone(),(Stack<Feature>) rightImage.fStack.clone()));
                super.mouseReleased(e);
                if(rightImage.feats.size() + rightImage.fStack.size() > leftImage.feats.size() + leftImage.fStack.size()){
                    Feature f = rightImage.feats.get(rightImage.feats.size() - 1);
                    rightImage.feats.remove(rightImage.feats.size() - 1);
                    rightImage.fStack.push(f);
                    rightImage.repaint();
                }else{
                    Feature f = leftImage.fStack.pop();
                    leftImage.feats.add(f);
                    leftImage.repaint();
                    rightImage.repaint();
                }
                if(leftImage.feats.size() == rightImage.feats.size() && leftImage.fStack.isEmpty() && rightImage.fStack.isEmpty()){
                    morphButton.setEnabled(true);
                }else{
                    morphButton.setEnabled(false);
                }
                undoButton.setEnabled(true);
            }

        });
        i = javax.imageio.ImageIO.read(new File(this.rightImagePath));
        i2 = i.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        this.bRightImage = (i.getWidth() > CONSTANT.MAX_WIDTH || i.getHeight() > CONSTANT.MAX_HEIGHT)? this.resize(i,CONSTANT.MAX_WIDTH, (int) Math.round(((double)CONSTANT.MAX_WIDTH / i.getWidth()) * i.getHeight())) : i;
        rightImage.setIcon(new ImageIcon(i2));
        gcRight.gridx = 0;
        gcRight.gridy = 0;
        rightImageContainer.add(rightImage, gcRight);
        rightImageBrowse = new JButton("Browse");
        rightImageBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rightBrowseButtonActionPerformed(e);
            }
        });
        gcRight.gridx = 0;
        gcRight.gridy = 1;
        rightImageContainer.add(rightImageBrowse, gcRight);

        gc.gridx = 0;
        gc.gridy = 0;
        frame.add(leftImageContainer, gc);
        gc.gridx = 1;
        gc.gridy = 0;
        frame.add(rightImageContainer, gc);

        morphButton = new JButton("Morph");
        morphButton.setEnabled(false);
        morphButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    morphButtonActionPerformed(e);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        gc.gridx = 0;
        gc.gridwidth = 3;
        gc.gridy = 1;
        frame.add(morphButton, gc);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearPerformed(e);
            }
        });
        gc.gridx = 1;
        gc.gridy = 1;
        frame.add(clearButton, gc);

        undoButton = new JButton("Undo");
        undoButton.setEnabled(false);
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                undoPerformed(e);
            }
        });
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.gridy = 1;
        frame.add(undoButton, gc);

        frame.setVisible(true);

    }

    private void undoPerformed(ActionEvent e) {
        GUIState s = this.states.pop();
        this.setState(s);
        if(this.states.isEmpty()){
            this.undoButton.setEnabled(false);
        }
    }

    private void clearPerformed(ActionEvent e) {

        this.setState(new GUIState());
        this.states = new Stack<GUIState>();

    }

    private void setState(GUIState guiState) {

        leftImage.feats = guiState.getLeftFeats();
        leftImage.fStack = guiState.getLeftStack();
        leftImage.repaint();

        rightImage.feats = guiState.getRightFeats();
        rightImage.fStack = guiState.getRightStack();
        rightImage.repaint();

    }

    private void morphButtonActionPerformed(ActionEvent e) throws IOException {

        //Dimension dLeft = this.getImageDim(leftImagePath);
        Dimension dLeft = new Dimension();
        dLeft.setSize(new ImageIcon(this.bLeftImage).getIconWidth(), new ImageIcon(this.bLeftImage).getIconHeight());

        ArrayList<Feature> feats = new ArrayList<Feature>();

        //formatting all the feature lines to fit the original image
        for(int i = 0; i < leftImage.feats.size(); i++){

            Feature fLeft = leftImage.feats.get(i);

            double leftStartX = fLeft.getStart().getX();
            double leftStartY = fLeft.getStart().getY();

            fLeft.getStart().setX(Math.round((leftStartX/300) * dLeft.getWidth()));
            fLeft.getStart().setY(Math.round((leftStartY/300) * dLeft.getHeight()));

            double leftEndX = fLeft.getEnd().getX();
            double leftEndY = fLeft.getEnd().getY();

            fLeft.getEnd().setX(Math.round((leftEndX/300) * dLeft.getWidth()));
            fLeft.getEnd().setY(Math.round((leftEndY/300) * dLeft.getHeight()));

            feats.add(fLeft);


            Feature fRight = rightImage.feats.get(i);

            double rightStartX = fRight.getStart().getX();
            double rightStartY = fRight.getStart().getY();

            fRight.getStart().setX(Math.round((rightStartX/300) * dLeft.getWidth()));
            fRight.getStart().setY(Math.round((rightStartY/300) * dLeft.getHeight()));

            double rightEndX = fRight.getEnd().getX();
            double rightEndY = fRight.getEnd().getY();

            fRight.getEnd().setX(Math.round((rightEndX/300) * dLeft.getWidth()));
            fRight.getEnd().setY(Math.round((rightEndY/300) * dLeft.getHeight()));

            feats.add(fRight);
        }

        ArrayList<ArrayList<Feature>> finalFeats = new ArrayList<ArrayList<Feature>>();
        finalFeats.add(feats);

        /*ArrayList<String> paths = new ArrayList<String>();
        paths.add(leftImagePath);
        paths.add(rightImagePath);*/
        ArrayList<Image> imgs = new ArrayList<Image>();
        imgs.add((Image) this.bLeftImage);
        imgs.add((Image) this.bRightImage);
        Beierneely b = new Beierneely(imgs, finalFeats, 45 );
        //b.loadImages(paths);
        ArrayList<BufferedImage> seq = b.morphSuccessiveImages(0);
        int index = 0;

        for (BufferedImage i : seq) {
            File outputfile = new File("image" + index + ".jpg");
            ImageIO.write(i, "jpg", outputfile);
            index++;
        }

        Runtime rt = Runtime.getRuntime();

        String[] command = {"/usr/local/Cellar/ffmpeg/4.3.1-with-options_5/bin/ffmpeg", "-r", "30", "-f", "image2", "-s", ((int)dLeft.getWidth()) + "x" + ((int)dLeft.getHeight()), "-i", "image%d.jpg", "-vcodec", "mpeg4", "-crf", "15", "-pix_fmt", "yuv420p", "output" + System.currentTimeMillis() + ".mp4"};
        Process process = rt.exec(command);

    }

    private ByteBuffer getBufferFromImage(BufferedImage bi){
        ByteBuffer byteBuffer;
        DataBuffer dataBuffer = bi.getRaster().getDataBuffer();

        if (dataBuffer instanceof DataBufferByte) {
            byte[] pixelData = ((DataBufferByte) dataBuffer).getData();
            byteBuffer = ByteBuffer.wrap(pixelData);
        }
        else if (dataBuffer instanceof DataBufferUShort) {
            short[] pixelData = ((DataBufferUShort) dataBuffer).getData();
            byteBuffer = ByteBuffer.allocate(pixelData.length * 2);
            byteBuffer.asShortBuffer().put(ShortBuffer.wrap(pixelData));
        }
        else if (dataBuffer instanceof DataBufferShort) {
            short[] pixelData = ((DataBufferShort) dataBuffer).getData();
            byteBuffer = ByteBuffer.allocate(pixelData.length * 2);
            byteBuffer.asShortBuffer().put(ShortBuffer.wrap(pixelData));
        }
        else if (dataBuffer instanceof DataBufferInt) {
            int[] pixelData = ((DataBufferInt) dataBuffer).getData();
            byteBuffer = ByteBuffer.allocate(pixelData.length * 4);
            byteBuffer.asIntBuffer().put(IntBuffer.wrap(pixelData));
        }
        else {
            throw new IllegalArgumentException("Not implemented for data buffer type: " + dataBuffer.getClass());
        }

        return byteBuffer;

    }

    private void leftBrowseButtonActionPerformed(ActionEvent e) {

        JFileChooser fc = new JFileChooser("./");
        int res = fc.showOpenDialog(null);
        // We have an image!
        try {
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                setLeftTarget(file);
            } // Oops!
            else {
                JOptionPane.showMessageDialog(null,
                        "You must select one image to be the reference.", "Aborting...",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception iOException) {
        }

    }

    public void setLeftTarget(File reference)
    {
        try {
            BufferedImage i = javax.imageio.ImageIO.read(reference);
            Image img = i.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            this.bLeftImage = (i.getWidth() > CONSTANT.MAX_WIDTH || i.getHeight() > CONSTANT.MAX_HEIGHT)? this.resize(i,CONSTANT.MAX_WIDTH, (int) Math.round(((double)CONSTANT.MAX_WIDTH / i.getWidth()) * i.getHeight())) : i;
            leftImage.setIcon(new ImageIcon(img));
            leftImagePath = reference.getAbsolutePath();

        } catch (IOException ex) {
            //Logger.getLogger(MainAppFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void rightBrowseButtonActionPerformed(ActionEvent e) {

        JFileChooser fc = new JFileChooser("./");
        int res = fc.showOpenDialog(null);
        // We have an image!
        try {
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                setRightTarget(file);
            } // Oops!
            else {
                JOptionPane.showMessageDialog(null,
                        "You must select one image to be the reference.", "Aborting...",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception iOException) {
        }

    }

    public void setRightTarget(File reference)
    {
        try {
            BufferedImage i = javax.imageio.ImageIO.read(reference);
            Image img = i.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            this.bRightImage = (i.getWidth() > CONSTANT.MAX_WIDTH || i.getHeight() > CONSTANT.MAX_HEIGHT)? this.resize(i,CONSTANT.MAX_WIDTH, (int) Math.round(((double)CONSTANT.MAX_WIDTH / i.getWidth()) * i.getHeight())) : i;
            rightImage.setIcon(new ImageIcon(img));
            rightImagePath = reference.getAbsolutePath();

        } catch (IOException ex) {
            //Logger.getLogger(MainAppFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Dimension getImageDim(final String path) {

        Dimension result = null;
        String suffix = this.getFileSuffix(path);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(new File(path));
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                result = new Dimension(width, height);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                reader.dispose();
            }
        } else {
            System.out.println("No reader found for given format: " + suffix);
        }
        return result;
    }

    private String getFileSuffix(final String path) {
        String result = null;
        if (path != null) {
            result = "";
            if (path.lastIndexOf('.') != -1) {
                result = path.substring(path.lastIndexOf('.'));
                if (result.startsWith(".")) {
                    result = result.substring(1);
                }
            }
        }
        return result;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
