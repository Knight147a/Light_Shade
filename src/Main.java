import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
public class Main extends JPanel {
    private int x1 = 50;
    private int y1 = 50;
    private static int firstEndX;
    private static int firstEndY;
    private static int maxDistance = 700;

    public Main() {
        setBackground(Color.BLACK);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color startColor = Color.WHITE;
        Color endColor = Color.BLACK;

        g2d.setColor(Color.RED);
        Line2D[] staticLineArray = new Line2D[6];
        staticLineArray[0] = new Line2D.Double(100, 400, 400, 500);
        g2d.drawLine(100, 400, 400, 500);

        staticLineArray[1] = new Line2D.Double(400, 100, 400, 500);
        g2d.drawLine(400, 100, 400, 500);

        staticLineArray[2] = new Line2D.Double(900, 100, 700, 500);
        g2d.drawLine(900, 100, 700, 500);

        staticLineArray[3] = new Line2D.Double(450, 700, 850, 600);
        g2d.drawLine(450, 700, 850, 600);

        staticLineArray[4] = new Line2D.Double(1400, 400, 1100, 700);
        g2d.drawLine(1400, 400, 1100, 700);

        staticLineArray[5] = new Line2D.Double(1000, 400, 600, 800);
        g2d.drawLine(1000, 400, 600, 800);



        for (double angle = 0; angle < 360; angle += 4) {
            double radians = Math.toRadians(angle);
            int endX = (int) (x1 + maxDistance * Math.cos(radians));
            int endY = (int) (y1 - maxDistance * Math.sin(radians));
            firstEndX = endX;
            firstEndY = endY;
            double distanceVector = maxDistance;
            Line2D tempLine = new Line2D.Double(x1, y1, endX, endY);
            for (Line2D line : staticLineArray) {
                if(tempLine.intersectsLine(line)){
                    double x11 = tempLine.getX1();
                    double y11 = tempLine.getY1();
                    double x22 = tempLine.getX2();
                    double y22 = tempLine.getY2();
                    double x33 = line.getX1();
                    double y33 = line.getY1();
                    double x44 = line.getX2();
                    double y44 = line.getY2();

                    double det = (x11 - x22) * (y33 - y44) - (y11 - y22) * (x33 - x44);

                    if (det != 0) {
                        double dx = x22 - x11;
                        double dy = y22 - y11;

                        double s = ((x11 - x33) * (y33 - y44) - (y11 - y33) * (x33 - x44)) / det;

                        double px = x11 + s * dx;
                        double py = y11 + s * dy;

                        double tmpDistance = Math.sqrt(Math.pow(px - x1, 2) + Math.pow( py - y1, 2));
                        if(distanceVector > tmpDistance){
                            distanceVector = tmpDistance;
                            endX = (int) px-1;
                            endY = (int) py;
                        }
                    }
                }
            }

            GradientPaint gradient = new GradientPaint(x1, y1, startColor, firstEndX, firstEndY, endColor);
            g2d.setPaint(gradient);
            g2d.drawLine(x1, y1, endX, endY);
            if (distanceVector < maxDistance) {
                g2d.setPaint(Color.blue);
                g2d.drawOval(endX, endY, 1, 1);
            }
        }

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Fading Line Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 1000);
        Main panel = new Main();
        frame.add(panel);
        frame.setVisible(true);
    }
}

