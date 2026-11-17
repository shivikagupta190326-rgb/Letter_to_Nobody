package ocean.ui;
import javax.swing.*;import java.awt.*;
/** Shared painted ocean backdrop with gentle animated glimmers. */
public class OceanPanel extends JPanel {
 private float phase=0;
 public OceanPanel(){setOpaque(false);new javax.swing.Timer(70,e->{phase+=.012f;repaint();}).start();}
 protected void paintComponent(Graphics graphics){super.paintComponent(graphics);Graphics2D g=(Graphics2D)graphics.create();int w=getWidth(),h=getHeight();g.setPaint(new GradientPaint(0,0,Theme.NAVY,0,h,new Color(16,87,124)));g.fillRect(0,0,w,h);g.setColor(new Color(52,173,194,22));for(int i=0;i<5;i++){int y=(int)(h*(.54+i*.105)+Math.sin(phase+i)*8);g.fillOval(-100,y,w+200,80);}g.setColor(new Color(215,255,252,50));for(int i=0;i<28;i++){int x=(i*173+37)%Math.max(w,1),y=(int)((i*91+phase*23)%(h+30));g.fillOval(x,y,2+(i%3),2+(i%3));}g.dispose();}
}
