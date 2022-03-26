package com.company;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.border.Border;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO.*;
import java.awt.image.*;
import java.io.File;

public class FractalExplorer {
    private final int displaySize;
    private final JImageDisplay jImageDisplay;
    private FractalGenerator fractalGenerator;
    private final Rectangle2D.Double range;

    private JButton resetButton;
    private JButton saveButton;
    private JComboBox comboBox;

    private int rowsRemaining;

    public FractalExplorer(int size) {
        this.displaySize = size;
        this.jImageDisplay = new JImageDisplay(displaySize, displaySize);
        this.range = new Rectangle2D.Double();
        fractalGenerator = new Mandelbrot();
        fractalGenerator.getInitialRange(range);
    }

    public void createAndShowGUI() {
        jImageDisplay.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractals");

        comboBox = new JComboBox();

        FractalGenerator mandelbrot = new Mandelbrot();
        FractalGenerator tricorn = new Tricorn();
        FractalGenerator burningShip = new BurningShip();

        comboBox.addItem(mandelbrot);
        comboBox.addItem(tricorn);
        comboBox.addItem(burningShip);

        ButtonHandler fractalChooser = new ButtonHandler();
        comboBox.addActionListener(fractalChooser);

        JPanel header = new JPanel();
        JLabel fractalsLabel = new JLabel("fractal:");
        header.add(fractalsLabel, BorderLayout.WEST);
        header.add(comboBox);


        JPanel footerButtonPanel = new JPanel();
        frame.add(footerButtonPanel, BorderLayout.SOUTH);

        resetButton = new JButton("Reset");
        saveButton = new JButton("Save");
        footerButtonPanel.add(resetButton);
        footerButtonPanel.add(saveButton);

        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        MouseHandler clickHandler = new MouseHandler();
        frame.addMouseListener(clickHandler);

        frame.add(header, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(jImageDisplay, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);


    }

    public void drawFractal() {
        enableUI(false);
        rowsRemaining = displaySize;

        for (int x = 0; x < displaySize; x++) {
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }
    }

    private void enableUI(boolean val) {
        comboBox.setEnabled(val);
        resetButton.setEnabled(val);
        saveButton.setEnabled(val);
    }

    //Внутренний класс, предназначенный чисто для обработки ивента кнопки сброса
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) { //сбрасываем диапазон к начальному и рисуем фрактал заново
            String command = e.getActionCommand();
            if (e.getSource() instanceof JComboBox) {
                JComboBox source = (JComboBox) e.getSource();
                fractalGenerator = (FractalGenerator) source.getSelectedItem();
                fractalGenerator.getInitialRange(range);
                drawFractal();

            } else if (command.equals("Reset")) {
                fractalGenerator.getInitialRange(range);
                drawFractal();

            } else if (command.equals("Save")) {

                // Разрешает пользователю выбирать файл для сохранения
                JFileChooser myFileChooser = new JFileChooser();
                myFileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

                myFileChooser.setAcceptAllFileFilterUsed(false);

                // Открывает окно "Сохранить файл", в котором пользователь может выбрать
                // каталог и файл для сохранения.
                int userSelection = myFileChooser.showSaveDialog(jImageDisplay);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File file = myFileChooser.getSelectedFile();

                    // Попытка сохранения изображения фрактала на диск
                    try {
                        BufferedImage displayImage = jImageDisplay.getImage();
                        ImageIO.write(displayImage, "png", file);
                    }
                    // Отлов исключений и вывод их сообщения на экран
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(jImageDisplay,
                                exception.getMessage(), "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
        private class MouseHandler extends MouseAdapter {
            /**
             * Когда обработчик получает событие щелчка мыши, он сопоставляет
             * координаты клика в область фрактала
             * отображаются, а затем вызывается функция CenterAndZoomRange() класса.
             * области с координатами, по которым щелкнули, и масштабом 0,5.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                //работа метода прерывается, если rowsRemaining ещё не равна нулю
                if (rowsRemaining != 0) {
                    return;
                }
                int x = e.getX();
                double xCoord = fractalGenerator.getCoord(range.x, range.x + range.width, displaySize, x);
                int y = e.getY();
                double yCoord = fractalGenerator.getCoord(range.y, range.y + range.height, displaySize, y);

                fractalGenerator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
                drawFractal();
            }
        }

        private class FractalWorker extends SwingWorker<Object, Object> {

            int yCoordinate;
            int[] computedRGBValues;

            private FractalWorker(int row) {
                yCoordinate = row;
            }

            /**
             * This method is called on a background thread.  It computes
             * the RGB value for all the pixels in 1 row and stores it in the
             * corresponding element of the integer array. Returns null.
             */
            protected Object doInBackground() {

                computedRGBValues = new int[displaySize];

                for (int i = 0; i < computedRGBValues.length; i++) {
                    double xCoord = fractalGenerator.getCoord(range.x, range.x + range.width, displaySize, i);
                    double yCoord = fractalGenerator.getCoord(range.y, range.y + range.height, displaySize, yCoordinate);
                    int iteration = fractalGenerator.numIterations(xCoord, yCoord);
                    if (iteration == -1) {
                        computedRGBValues[i] = 0;
                    } else {

                        float hue = 0.7f + (float) iteration / 200f;
                        int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                        computedRGBValues[i] = rgbColor;
                    }
                }
                return null;

            }

            protected void done() {
                for (int i = 0; i < computedRGBValues.length; i++) {
                    jImageDisplay.drawPixel(i, yCoordinate, computedRGBValues[i]);
                }
                jImageDisplay.repaint(0, 0, yCoordinate, displaySize, 1);

                // Decrement rows remaining.  If 0, call enableUI(true)
                rowsRemaining--;
                if (rowsRemaining == 0) {
                    enableUI(true);
                }
            }
        }

}
