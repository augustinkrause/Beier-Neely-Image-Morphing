import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javax.imageio.ImageIO;

public class Beierneely {

    public ArrayList<Image> images;
    public ArrayList<ArrayList<Feature>> features;
    public int nSteps;
    public double a = 3;
    public double b = 2.0;
    public double p = 1.0;

    public Beierneely(ArrayList<Image> images, ArrayList<ArrayList<Feature>> features, int nSteps) {
        this.images = images;
        this.features = features;
        this.nSteps = nSteps;
    }

    public Beierneely(int nSteps, ArrayList<ArrayList<Feature>> features) {
        this.images = new ArrayList<Image>();
        this.features = features;
        this.nSteps = nSteps;
    }

    public Beierneely(ArrayList<Image> images, int nSteps) {
        this.images = images;
        this.features = new ArrayList<ArrayList<Feature>>();
        this.nSteps = nSteps;
    }

    public Beierneely(int nSteps) {
        this.images = new ArrayList<Image>();
        this.features = new ArrayList<ArrayList<Feature>>();
        this.nSteps = nSteps;
    }

    public Beierneely(ArrayList<Image> images, ArrayList<ArrayList<Feature>> features, int nSteps, double a, double b, double p) {
        this.images = images;
        this.features = features;
        this.nSteps = nSteps;
        this.a = a;
        this.b = b;
        this.p = p;
    }

    public Beierneely(ArrayList<Image> images, int nSteps, double a, double b, double p) {
        this.images = images;
        this.features = new ArrayList<ArrayList<Feature>>();
        this.nSteps = nSteps;
        this.a = a;
        this.b = b;
        this.p = p;
    }

    public Beierneely(int nSteps, double a, double b, double p) {
        this.images = new ArrayList<Image>();
        this.features = new ArrayList<ArrayList<Feature>>();
        this.nSteps = nSteps;
        this.a = a;
        this.b = b;
        this.p = p;
    }

    public void loadImages(ArrayList<String> paths) throws IOException {

        for(String path : paths){
            Image image = javax.imageio.ImageIO.read(new File(path));
            this.images.add(image);
        }

    }

    public void loadFeatures(String path) throws IOException {

        In in = new In(new File(path));
        int nImages = in.readInt();
        for(int i = 0; i < nImages - 1; i++){
            int nFeatures = in.readInt();
            ArrayList<Feature> feats = new ArrayList<Feature>();
            for(int j = 0; j < nFeatures; j++){

                for(int k = 0; k < 2; k++) {
                    Vector2d start = new Vector2d(in.readInt(), in.readInt());
                    Vector2d end = new Vector2d(in.readInt(), in.readInt());
                    Feature f = new Feature(start, end);
                    feats.add(f);
                }

            }
            this.features.add(feats);
        }


    }

    //morphs image at index start to image start + 1
    public ArrayList<BufferedImage> morphSuccessiveImages(int start){

        BufferedImage currentSource = (BufferedImage) this.images.get(start);
        BufferedImage currentDest = (BufferedImage) this.images.get(start + 1);
        ArrayList<BufferedImage> sequence = new ArrayList<BufferedImage>();
        sequence.add((BufferedImage) this.images.get(start));
        for(int i = 1; i <= this.nSteps; i++){
            long millies = System.currentTimeMillis();
            ArrayList<Feature> interpolated = this.interpolate(this.features.get(start), i);
            BufferedImage warpedSource = this.warp(currentSource, interpolated, this.features.get(start), true);
            BufferedImage warpedDest = this.warp(currentDest, interpolated, this.features.get(start), false);
            sequence.add(this.blend(warpedSource, warpedDest, i));
            System.out.println("Step " + i + " took " + (System.currentTimeMillis() - millies) + " milliseconds");
        }

        System.out.println("Finished!");
        return sequence;
    }

    public ArrayList<Feature> interpolate(ArrayList<Feature> feats, int timestep){

        ArrayList<Feature> interpolated = new ArrayList<Feature>();
        //loop through all the PAIRS of directed line segments
        for(int i = 0; i < feats.size(); i+=2){

            //calculate all the increments to get from one point to another over n steps
            double incrStartX = (feats.get(i + 1).getStart().getX() - feats.get(i).getStart().getX())/this.nSteps;
            double incrStartY = (feats.get(i + 1).getStart().getY() - feats.get(i).getStart().getY())/this.nSteps;
            double incrEndX = (feats.get(i + 1).getEnd().getX() - feats.get(i).getEnd().getX())/this.nSteps;
            double incrEndY = (feats.get(i + 1).getEnd().getY() - feats.get(i).getEnd().getY())/this.nSteps;

            Vector2d newStart = new Vector2d(feats.get(i).getStart().getX() + (incrStartX * timestep), feats.get(i).getStart().getY() + (incrStartY * timestep));
            Vector2d newEnd = new Vector2d(feats.get(i).getEnd().getX() + (incrEndX * timestep), feats.get(i).getEnd().getY() + (incrEndY * timestep));
            Feature newSegment = new Feature(newStart, newEnd);

            interpolated.add(newSegment);
        }

        return interpolated;
    }

    //depending on wether or not the input image is source or not this method warps source towards dest or vice versa
    public BufferedImage warp(BufferedImage source, ArrayList<Feature> interpolated, ArrayList<Feature> feats, boolean isSource){

        BufferedImage warped = new BufferedImage(source.getWidth(),source.getHeight(),BufferedImage.TYPE_INT_RGB);

        for (int j = 0; j < source.getWidth() * source.getHeight(); j++) {
            int y = j / source.getWidth();
            int x = j - (y * source.getWidth());
            Vector2d dSum = new Vector2d(0.0, 0.0);
            double wSum = 0;
            Vector2d X = new Vector2d((double) x,(double) y);

            for(int i = 0; i < feats.size(); i+=2){
                Feature fSource = feats.get(isSource? i : i + 1);
                Feature fDest = interpolated.get(i/2);
                Vector2d QMinusP = fDest.getEnd().subtract(fDest.getStart());
                Vector2d XMinusP = X.subtract(fDest.getStart());
                double gamma = (XMinusP.dotProduct(QMinusP))/QMinusP.magnitude();
                double u = gamma/QMinusP.magnitude();
                Vector2d QMinusPPerp = QMinusP.perpendicular();
                double v = (XMinusP.dotProduct(QMinusPPerp.scale(QMinusP.magnitude()/QMinusPPerp.magnitude())))/QMinusP.magnitude();
                Vector2d QPrevMinusPPrev = fSource.getEnd().subtract(fSource.getStart());
                Vector2d QPrevMinusPPrevPerp = QPrevMinusPPrev.perpendicular();
                Vector2d XPrev = fSource.getStart().add(QPrevMinusPPrev.scale(u)).add(QPrevMinusPPrevPerp.scale(QPrevMinusPPrev.magnitude()/QPrevMinusPPrevPerp.magnitude()).scale(v).scale(1/QPrevMinusPPrev.magnitude()));
                Vector2d displacement = XPrev.subtract(X);
                double weight = Math.pow(Math.pow(QMinusP.magnitude(), this.p)/(this.a + v), this.b);
                dSum = dSum.add(displacement.scale(weight));
                wSum += weight;
            }

            Vector2d XPrev = X.add(dSum.scale(1/wSum));

            //if XPrev contains non-integer coordinate then find the pixel coordinate XIPrev by interpolating the neighbours of XPrev and rounding the interpolation result
            if(XPrev.getX() % 1 != 0 || XPrev.getY() % 1 != 0){
                XPrev.setX(Math.round(XPrev.getX()));
                XPrev.setY(Math.round(XPrev.getY()));
            }

            //if XPrev falls outside the image domain then find the pixel coordinate closest to XPrev on the boundary of the source image
            XPrev.setX(XPrev.getX() >= source.getWidth()? source.getWidth() - 1 : XPrev.getX());
            XPrev.setX(XPrev.getX() < 0? 0 : XPrev.getX());
            XPrev.setY(XPrev.getY() >= source.getHeight()? source.getHeight() - 1 : XPrev.getY());
            XPrev.setY(XPrev.getY() < 0? 0 : XPrev.getY());

            warped.setRGB((int)X.getX(), (int)X.getY(), source.getRGB((int)XPrev.getX(), (int)XPrev.getY()));
        }

        return warped;
    }

    public BufferedImage blend(BufferedImage source, BufferedImage dest, int step){

        BufferedImage blended = new BufferedImage(source.getWidth(),source.getHeight(),BufferedImage.TYPE_INT_RGB);

        for (int j = 0; j < source.getWidth() * source.getHeight(); j++) {

            int y = j / source.getWidth();
            int x = j - (y * source.getWidth());
            int clrSource = source.getRGB(x, y);
            int clrDest = dest.getRGB(x, y);
            double alpha = ((double) step)/this.nSteps;

            final int redS = (clrSource & 0x00ff0000) >> 16;
            final int greenS = (clrSource & 0x0000ff00) >> 8;
            final int blueS = clrSource & 0x000000ff;

            final int redD = (clrDest & 0x00ff0000) >> 16;
            final int greenD = (clrDest & 0x0000ff00) >> 8;
            final int blueD = clrDest & 0x000000ff;

            Color newClr = new Color((int) ((alpha * redD) + (1 - alpha)  * redS), (int) ((alpha * greenD) + (1 - alpha)  * greenS), (int) ((alpha * blueD) + (1 - alpha)  * blueS));
            blended.setRGB(x, y, newClr.getRGB());

        }

        return blended;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        GUI gui = new GUI();

    }

}
