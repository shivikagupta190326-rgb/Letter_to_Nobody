package ocean.ui;
import javax.swing.*;import javax.swing.border.*;import java.awt.*;
public final class Theme {
 public static final Color NAVY=new Color(9,27,58),BLUE=new Color(13,55,94),AQUA=new Color(105,230,221),LAV=new Color(194,173,255),WHITE=new Color(245,251,255),MUTED=new Color(196,216,231);
 public static final Font TITLE=new Font("Serif",Font.BOLD,38),HEAD=new Font("SansSerif",Font.BOLD,24),BODY=new Font("SansSerif",Font.PLAIN,15);
 private Theme(){}
 public static JLabel label(String s,int size,Color c,boolean bold){JLabel l=new JLabel(s);l.setFont(new Font("SansSerif",bold?Font.BOLD:Font.PLAIN,size));l.setForeground(c);return l;}
 public static JButton button(String text,boolean primary){JButton b=new JButton(text);b.setFont(new Font("SansSerif",Font.BOLD,15));b.setForeground(primary?NAVY:WHITE);b.setBackground(primary?AQUA:new Color(255,255,255,28));b.setFocusPainted(false);b.setBorder(new CompoundBorder(new LineBorder(primary?AQUA:new Color(150,210,230,100),1,true),new EmptyBorder(12,22,12,22)));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
 public static JPanel card(){JPanel p=new JPanel();p.setOpaque(true);p.setBackground(new Color(255,255,255,24));p.setBorder(new CompoundBorder(new LineBorder(new Color(210,245,255,70),1,true),new EmptyBorder(24,28,24,28)));return p;}
}
