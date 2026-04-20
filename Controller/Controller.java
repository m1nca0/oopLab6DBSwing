package Controller;

import javax.swing.JOptionPane;

import DataBase.DBsamples;
import Model.Daw;
import Model.DawInOut;
import Model.Hat;
import Model.Kick;
import Model.Sample;
import Model.Snare;

import java.sql.SQLException;
import java.util.List;

import javax.swing.*;

public class Controller {
    private Daw daw;
    private DawInOut inOut;

    public Controller(Daw dawContr, DawInOut inOut) throws SQLException {
        this.daw = dawContr;
        List<Sample> savedSamples = DBsamples.loadAllSamples();
        for (Sample s : savedSamples) {
            this.daw.CreateSample(s);
        }
    }

    public void saveProjectToFile(String way) {
        if (!way.toLowerCase().endsWith(".txt")) {
            way += ".txt";
        }
        inOut.saveProject(daw, way);
    }

    public void makeDefSamplesDB() throws SQLException {
        DBsamples.writeDefaultSamples();
        List<Sample> savedSamples = DBsamples.loadAllSamples();
        for (Sample s : savedSamples) {
            this.daw.CreateSample(s);
        }
    }

    public void showDeleteMenu(String name, String type) throws SQLException {
        if (name.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Для удаления заполните все поля!");
            return;
        }
        JOptionPane.showMessageDialog(null, "Сэмпл успешно удален!");
        this.daw.findSample(name, type, false);
        if("KICK".equals(type)){
            DBsamples.deleteKickByName(name);
        } else if("SNARE".equals(type)){
            DBsamples.deleteSnareByName(name);
        } else{
            DBsamples.deleteHatByName(name);
        }
    }

    public void showKickDialog(String nameKick, String lenKick, String volumeKick, String lowFrequencyKick,
            String highFrequencyKick, String bassLevelKick) {
        if (nameKick.isEmpty() ||
                lenKick.isEmpty() ||
                volumeKick.isEmpty() ||
                lowFrequencyKick.isEmpty() ||
                highFrequencyKick.isEmpty() ||
                bassLevelKick.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Для добавления сэмпла заполните все поля!");

            return;
        }
        try {
            String name = nameKick.trim();
            int len = Integer.parseInt(lenKick.trim());
            double volume = Double.parseDouble(volumeKick.trim());
            int lowFrequency = Integer.parseInt(lowFrequencyKick.trim());
            int highFrequency = Integer.parseInt(highFrequencyKick.trim());
            int bassLevel = Integer.parseInt(bassLevelKick.trim());

            Kick kick = new Kick(name, len, volume, lowFrequency, highFrequency, bassLevel);
            DBsamples.insertKick(kick);
            this.daw.CreateSample(kick);
            JOptionPane.showMessageDialog(null, "Kick - " + name + " успешно добавлен!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректные значения в поля \n Если поле подразумевает числовое значение, не вводите текст \n Для дробных значений используйте '.'");
        }
    }

    public void showSnareDialog(String nameSnare, String lenSnare, String volumeSnare, String lowFrequencySnare,
            String highFrequencySnare, String resonanceSnare, String punchSnare) {
        if (nameSnare.isEmpty() ||
                lenSnare.isEmpty() ||
                volumeSnare.isEmpty() ||
                lowFrequencySnare.isEmpty() ||
                highFrequencySnare.isEmpty() ||
                resonanceSnare.isEmpty() ||
                punchSnare.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Для добавления сэмпла заполните все поля!");

            return;
        }
        try {
            String name = nameSnare;
            int len = Integer.parseInt(lenSnare);
            double volume = Double.parseDouble(volumeSnare);
            int lowFrequency = Integer.parseInt(lowFrequencySnare);
            int highFrequency = Integer.parseInt(highFrequencySnare);
            int resonance = Integer.parseInt(resonanceSnare);
            int punch = Integer.parseInt(punchSnare);

            Snare snare = new Snare(name, len, volume, lowFrequency, highFrequency, resonance, punch);
            DBsamples.insertSnare(snare);
            this.daw.CreateSample(snare);
            JOptionPane.showMessageDialog(null, "Snare - " + name + " успешно добавлен!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректные значения в поля \n Если поле подразумевает числовое значение, не вводите текст \n Для дробных значений используйте '.'");
        }
    }

    public void showHatDialog(String nameHat, String lenHat, String volumeHat, String lowFrequencyHat,
            String highFrequencyHat, String tailLengthHat, String closedHat) {
        if (nameHat.isEmpty() ||
                lenHat.isEmpty() ||
                volumeHat.isEmpty() ||
                lowFrequencyHat.isEmpty() ||
                highFrequencyHat.isEmpty() ||
                tailLengthHat.isEmpty() ||
                closedHat.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Для добавления сэмпла заполните все поля!");
            return;

        }
        try {
            String name = nameHat;
            int len = Integer.parseInt(lenHat);
            double volume = Double.parseDouble(volumeHat);
            int lowFrequency = Integer.parseInt(lowFrequencyHat);
            int highFrequency = Integer.parseInt(highFrequencyHat);
            int tailLength = Integer.parseInt(tailLengthHat);
            boolean closed = closedHat == "Закрытый" ? false : true;
            Hat hat = new Hat(name, len, volume, lowFrequency, highFrequency, tailLength, closed);
            DBsamples.insertHat(hat);
            this.daw.CreateSample(hat);
            JOptionPane.showMessageDialog(null, "Hat - " + name + " успешно добавлен!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректные значения в поля \n Если поле подразумевает числовое значение, не вводите текст \n Для дробных значений используйте '.'");
        }

    }

}
