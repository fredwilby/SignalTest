/*
 * Copyright (c) Fred Wilby 2015. All rights reserved
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


/**
 * Created by Fred on 2/13/2015.
 */
public class SignalGraph extends JFrame
{
    public static final int BITS = 6, SPB = 200, F1 = 4410, F2 = 2205;


    public SignalGraph(ToneEncoder T)
    {
        super("SignalGraph");

        SignalPane sp = new SignalPane(T.getSample());

        getContentPane().add(sp);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    class SignalPane extends JPanel
    {
        double[] windowProps = new double[]{ -4, 4, -1, 1 };

        Polygon signal;

        public SignalPane(double[] samples)
        {
            super();
            setPreferredSize(new Dimension(BITS * SPB, 100));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for(int x = 0; x < signal.xpoints.length; x++)
                    {
                        if(signal.xpoints[x] == e.getX())
                        {
                            System.out.println("X: "+signal.xpoints[x] + ", Y: "+signal.ypoints[x]);
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                }
            });


            signal = new Polygon();

            for(int b = 0; b < samples.length; b++)
            {
                int pt = parsePoint(samples[b]);
                signal.addPoint(b, pt);
            }



        }

        public int parsePoint(double coord)
        {
            return (int) (100*(coord+2)/(4));

        }


        @Override
        protected void paintComponent(Graphics G)
        {
            G.setColor(Color.white);
            G.fillRect(0, 0, getWidth(), getHeight());

            // axes
            G.setColor(Color.gray);
            G.drawLine(0, 50, SPB*BITS, 50);

            for(int b = 1; b < BITS; b++)
            {
                G.drawLine(SPB*b, 0, SPB*b, 100);
            }

            G.setColor(Color.black);
            G.drawPolyline(signal.xpoints, signal.ypoints, signal.npoints);


        }





    };





    public static void main(String[] args)
    {

        SignalProperties.SignalPropertiesBuilder build = new SignalProperties.SignalPropertiesBuilder();
        build.samplesPerBit(SPB);
        build.frequencies(new int[] {F1, F2});

        boolean[] barr = new boolean[BITS];

        for(int b = 0; b < BITS; b++)
        {
            barr[b] = (b%2 == 0);
        }

        ToneEncoder t = new ToneEncoder(barr, build.build());

        new SignalGraph(t);
    }

}
