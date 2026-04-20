package View;

import javax.swing.*;
import javax.swing.table.TableRowSorter;

import Controller.Controller;
import Model.Daw;
import Model.DawInOut;
import Model.Sample;
import DataBase.DBsamples;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

public class DawView extends JFrame {

    SearchListener searchListener;

    private Daw daw;
    private DawInOut inOut;
    private MyTable myTableModel;
    private int rowIndex = 0;
    private JFileChooser fileChooser = new JFileChooser();
    private Controller controller;
    private JButton filterButton = new JButton("Параметр поиска");

    JTextField nameSample = new JTextField();
    JTextField typeSample = new JTextField();

    JTextField nameKick = new JTextField(15);
    JTextField lenKick = new JTextField(15);
    JTextField volumeKick = new JTextField(15);
    JTextField lowFrequencyKick = new JTextField(15);
    JTextField highFrequencyKick = new JTextField(15);
    JTextField bassLevelKick = new JTextField(15);

    JTextField nameSnare = new JTextField(15);
    JTextField lenSnare = new JTextField(15);
    JTextField volumeSnare = new JTextField(15);
    JTextField lowFrequencySnare = new JTextField(15);
    JTextField highFrequencySnare = new JTextField(15);
    JTextField resonanceSnare = new JTextField(15);
    JTextField punchSnare = new JTextField(15);

    JTextField nameHat = new JTextField(15);
    JTextField lenHat = new JTextField(15);
    JTextField volumeHat = new JTextField(15);
    JTextField lowFrequencyHat = new JTextField(15);
    JTextField highFrequencyHat = new JTextField(15);
    JTextField tailLengthHat = new JTextField(15);
    JTextField closedHat = new JTextField(15);

    public DawView() throws ClassNotFoundException, SQLException {
        DBsamples.connectionDB();
        DBsamples.createTables();

        this.daw = new Daw();
        this.inOut = new DawInOut();
        this.myTableModel = new MyTable(this.daw);
        this.controller = new Controller(this.daw, this.inOut);

        JTable table = new JTable(myTableModel);
        table.setAutoCreateRowSorter(true);
        TableRowSorter<MyTable> sorter = new TableRowSorter<>(myTableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        JButton openButton = new JButton("Открыть");
        JButton saveButton = new JButton("Сохранить");
        JButton addButton = new JButton("Добавить");
        JButton deleteButton = new JButton("Удалить");
        JLabel searchLabel = new JLabel(" Поиск: ");
        JTextField searchField = new JTextField(15);

        searchListener = new SearchListener(table, sorter, searchField, rowIndex);
        searchField.getDocument().addDocumentListener(searchListener);

        setSize(1400, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(true);
        toolBar.add(openButton);
        toolBar.addSeparator();
        toolBar.add(saveButton);
        toolBar.addSeparator();
        toolBar.add(addButton);
        toolBar.addSeparator();
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(filterButton);
        toolBar.addSeparator();
        toolBar.add(searchLabel);
        toolBar.add(searchField);

        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddMenu(addButton);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showDeleteMenu();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFilterMenu(filterButton);
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileList();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProjectToFile();
            }
        });

        setVisible(true);
    }

    private void saveProjectToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить проект");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String way = fileToSave.getAbsolutePath();

            controller.saveProjectToFile(way);
        }
    }

    private void showFileList() {
        int response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.daw = controller.showFileList(file.getAbsolutePath());
            myTableModel.setDaw(this.daw);
            myTableModel.fireTableDataChanged();
        }
    }

    private void showFilterMenu(JButton button) {
        JPopupMenu filterMenu = new JPopupMenu();

        JMenuItem typeIteme = new JMenuItem("Тип");
        typeIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 1;
                filterButton.setText("Тип");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem nameIteme = new JMenuItem("Название");
        nameIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 2;
                filterButton.setText("Название");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem lenIteme = new JMenuItem("Длина");
        lenIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 3;
                filterButton.setText("Длина");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem volumeIteme = new JMenuItem("Громкость");
        volumeIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 4;
                filterButton.setText("Громкость");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem lowIteme = new JMenuItem("Низ");
        lowIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 5;
                filterButton.setText("Низ");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem highIteme = new JMenuItem("Вверх");
        highIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 6;
                filterButton.setText("Вверх");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem bassIteme = new JMenuItem("Басс");
        bassIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 7;
                filterButton.setText("Басс");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem resIteme = new JMenuItem("Резонанс");
        resIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 8;
                filterButton.setText("Резонанс");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem punchIteme = new JMenuItem("Удар");
        punchIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 9;
                filterButton.setText("Удар");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem tailIteme = new JMenuItem("Длина хвоста");
        tailIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 10;
                filterButton.setText("Длина хвоста");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });
        JMenuItem openCloseIteme = new JMenuItem("Закрытый/Открытый");
        openCloseIteme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowIndex = 11;
                filterButton.setText("Закрытый/Открытый");
                if (searchListener != null) {
                    searchListener.setRowIndex(rowIndex);
                }
            }

        });

        filterMenu.add(typeIteme);
        filterMenu.add(nameIteme);
        filterMenu.add(lenIteme);
        filterMenu.add(volumeIteme);
        filterMenu.add(lowIteme);
        filterMenu.add(highIteme);
        filterMenu.add(bassIteme);
        filterMenu.add(resIteme);
        filterMenu.add(punchIteme);
        filterMenu.add(tailIteme);
        filterMenu.add(openCloseIteme);
        filterMenu.show(button, 0, button.getHeight());
    }

    private String getNameSample() {
        return nameSample.getText().trim();
    }

    private String getTypeSample() {
        return typeSample.getText().trim();
    }

    private void showDeleteMenu() throws SQLException {
        JPanel deletePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        deletePanel.add(new JLabel("Название:"));
        deletePanel.add(nameSample);
        deletePanel.add(new JLabel("Тип:"));
        deletePanel.add(typeSample);

        int result = JOptionPane.showConfirmDialog(
                this,
                deletePanel,
                "Удаление сэмпла",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.showDeleteMenu(getNameSample(), getTypeSample());
            myTableModel.fireTableDataChanged();
        }
    }

    private void showAddMenu(JButton button) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem kickItem = new JMenuItem("Добавить KICK");
        kickItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showKickDialog();
            }
        });

        JMenuItem snareItem = new JMenuItem("Добавить SNARE");
        snareItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSnareDialog();
            }
        });

        JMenuItem hatItem = new JMenuItem("Добавить HAT");
        hatItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHatDialog();
            }
        });
        menu.add(kickItem);
        menu.add(snareItem);
        menu.add(hatItem);
        menu.show(button, 0, button.getHeight());
    }

    private String getNameKick() {
        return nameKick.getText().trim();
    }

    private String getLenKick() {
        return lenKick.getText().trim();
    }

    private String getVolumeKick() {
        return volumeKick.getText().trim();
    }

    private String getLowFrequencyKick() {
        return lowFrequencyKick.getText().trim();
    }

    private String getHighFrequencyKick() {
        return highFrequencyKick.getText().trim();
    }

    private String getBassLevelKick() {
        return bassLevelKick.getText().trim();
    }

    private void showKickDialog() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Название: "));
        panel.add(nameKick);
        panel.add(new JLabel("Длина (в милисекундах, 0-10000): "));
        panel.add(lenKick);
        panel.add(new JLabel("Громкость: "));
        panel.add(volumeKick);
        panel.add(new JLabel("Низ: "));
        panel.add(lowFrequencyKick);
        panel.add(new JLabel("Вверх: "));
        panel.add(highFrequencyKick);
        panel.add(new JLabel("Басс: "));
        panel.add(bassLevelKick);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Добавление kick'а",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.showKickDialog(getNameKick(), getLenKick(), getVolumeKick(), getLowFrequencyKick(),
                    getHighFrequencyKick(), getBassLevelKick());
            myTableModel.fireTableDataChanged();
        }
    }

    private String getNameSnare() {
        return nameSnare.getText().trim();
    }

    private String getLenSnare() {
        return lenSnare.getText().trim();
    }

    private String getVolumeSnare() {
        return volumeSnare.getText().trim();
    }

    private String getLowFrequencySnare() {
        return lowFrequencySnare.getText().trim();
    }

    private String getHighFrequencySnare() {
        return highFrequencySnare.getText().trim();
    }

    private String getResonanceSnare() {
        return resonanceSnare.getText().trim();
    }

    private String getPunchSnare() {
        return punchSnare.getText().trim();
    }

    private void showSnareDialog() {

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("Название: "));
        panel.add(nameSnare);
        panel.add(new JLabel("Длина (в милисекундах, 0-10000): "));
        panel.add(lenSnare);
        panel.add(new JLabel("Громкость: "));
        panel.add(volumeSnare);
        panel.add(new JLabel("Низ: "));
        panel.add(lowFrequencySnare);
        panel.add(new JLabel("Вверх: "));
        panel.add(highFrequencySnare);
        panel.add(new JLabel("Резонанс: "));
        panel.add(resonanceSnare);
        panel.add(new JLabel("Удар: "));
        panel.add(punchSnare);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Добавление snare'а",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.showSnareDialog(getNameSnare(), getLenSnare(), getVolumeSnare(), getLowFrequencySnare(),
                    getHighFrequencySnare(), getResonanceSnare(), getPunchSnare());
            myTableModel.fireTableDataChanged();
        }
    }

    private String getNameHat() {
        return nameHat.getText().trim();
    }

    private String getLenHat() {
        return lenHat.getText().trim();
    }

    private String getVolumeHat() {
        return volumeHat.getText().trim();
    }

    private String getLowFrequencyHat() {
        return lowFrequencyHat.getText().trim();
    }

    private String getHighFrequencyHat() {
        return highFrequencyHat.getText().trim();
    }

    private String getTailLengthHat() {
        return tailLengthHat.getText().trim();
    }

    private String getClosedHat() {
        return closedHat.getText().trim();
    }

    private void showHatDialog() {

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("Название: "));
        panel.add(nameHat);
        panel.add(new JLabel("Длина (в милисекундах, 0-10000): "));
        panel.add(lenHat);
        panel.add(new JLabel("Громкость: "));
        panel.add(volumeHat);
        panel.add(new JLabel("Низ: "));
        panel.add(lowFrequencyHat);
        panel.add(new JLabel("Вверх: "));
        panel.add(highFrequencyHat);
        panel.add(new JLabel("Длина хвоста: "));
        panel.add(tailLengthHat);
        panel.add(new JLabel("Открытый/Закрытый: "));
        panel.add(closedHat);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Добавление Hat'а",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            controller.showHatDialog(getNameHat(), getLenHat(), getVolumeHat(), getLowFrequencyHat(),
                    getHighFrequencyHat(), getTailLengthHat(), getClosedHat());
            myTableModel.fireTableDataChanged();
        }
    }
}