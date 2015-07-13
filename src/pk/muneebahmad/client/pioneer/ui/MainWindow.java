/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pk.muneebahmad.client.pioneer.ui;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import jxl.Cell;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import pk.muneebahmad.client.pioneer.model.PersonPersistence;
import pk.muneebahmad.client.pioneer.persistence.CachePersistence;
import pk.muneebahmad.client.pioneer.util.AppConfPersistence;
import pk.muneebahmad.client.pioneer.util.EmpPersistence;
import pk.muneebahmad.client.pioneer.util.EmpExtPersistence;
import pk.muneebahmad.client.pioneer.util.EmployeeDataPersistence;
import pk.muneebahmad.client.pioneer.util.ExcelLoader;
import pk.muneebahmad.client.pioneer.util.Log;
import pk.muneebahmad.client.pioneer.util.RASLoader;
import pk.muneebahmad.client.pioneer.util.SalaryInfo;
import pk.muneebahmad.client.pioneer.util.WorkbookLoader;

/**
 *
 * @author muneebahmad
 */
public class MainWindow extends javax.swing.JFrame {

    private static final long serialVersionUID = 4666343091626830286L;

    public static final String LAF_FILENAME = ".laf";
    public static final String CACHE_FILENAME = ".cache";
    public static final String CACHE = "0x5569854736";
    public static final String EMP_LIST_FILENAME = "emp.algo";
    public static final String EMP_EXT_FILENAME = "emp_ext.algo";
    public static final String EMP_DATA_FILENAME = "emp_data.algo";
    public static final String CONF_FILE_NAME = "conf.conf";
    public static final String SLR_FILENAME = "srl.algo";
    public static final String WORKBOOK_FILLENAME = "temp_wk.xls";
    public static final String FILE_PERSON_INFO = "person.algo";

    private static JTextField logField;
    private final ExcelLoader excelLoader;
    private final RASLoader rasLoader;
    private WorkbookLoader workLoader;
    private boolean loadAgain = false;

    private DefaultTableModel rasTableModel;
    private JTable rasTable;

    private DefaultTableModel enumTableModel;
    private JTable enumTable;

    private DefaultTableModel imsTableModel;
    private JTable imsTable;

    private DefaultTableModel empModel;
    private JTable empTable;

    String timeIn = "";
    String timeOut = "";

    private final ImageIcon icon = ImageIconFactory.makeImageIcon("gfx/question_icon.png", "Icon", MainWindow.class);

    /**
     * Creates new form MainWindow
     *
     * @param loadAgain
     */
    public MainWindow(boolean loadAgain) {
        this.loadAgain = loadAgain;
        initComponents();
        addWindowFeatures();
        checkCache();
        initUIComps();
        setProgress(30, "Loading");
        Log.log(MainWindow.class.getName(), "Initialized Bootstrap");
        excelLoader = ExcelLoader.getInstance("IMSDB data.xls");
        this.rasLoader = RASLoader.getInstance("RAS data.xls");
        makeEmpTable(false);
        t.start();
    }

    private void addWindowFeatures() {
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                makeExitDialog();
            }

        });

        this.jMenuItem6.setEnabled(false);
        this.jButton3.setEnabled(false);
        this.jComboBox1.setSelectedItem("Employee");
        
        this.jLabel1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.jLabel1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 0) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            URI uri = new URI("http://1-dot-muneeb-ahmad.appspot.com/controller?nav=connect");
                            desktop.browse(uri);
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Log.log(MainWindow.class.getName(), "http://1-dot-muneeb-ahmad.appspot.com/controller?nav=connect");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Log.log(MainWindow.class.getName(), "Logger in queue");
            }
        });
    }

    private void checkCache() {
        if (CachePersistence.isLoaded()) {
            if (!(CachePersistence.getLoadedCache().equals(CACHE))) {
                int response = JOptionPane.showConfirmDialog(null, "Your app version is not registered, please contact "
                        + "\n the vendor for more information",
                        "Warning Dialog", JOptionPane.OK_OPTION);
                if (response == JOptionPane.OK_OPTION) {
                    System.exit(0);
                } else {
                    System.exit(0);
                }
            }
        }
    }

    private void writeCache() {
        CachePersistence.write(CACHE_FILENAME, CACHE);
    }

    private void loadEmpData() {
        try {
            PersonPersistence.load(FILE_PERSON_INFO);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 1; i < rasLoader.getSheetRowNo(); i++) {

            EmpPersistence.add(rasLoader.getSheet().getCell(6, i).getContents(),
                    rasLoader.getSheet().getCell(2, i).getContents(),
                    rasLoader.getSheet().getCell(1, i).getContents(),
                    rasLoader.getSheet().getCell(5, i).getContents(),
                    "0", rasLoader.getSheet().getCell(8, i).getContents());

            String attType = rasLoader.getSheet().getCell(8, i).getContents();
            String dateIn = "";
            String present = "";

            if (attType.equals("Clock-in")) {
                dateIn = rasLoader.getSheet().getCell(6, i).getContents();
                timeIn = rasLoader.getSheet().getCell(7, i).getContents();
                timeOut = " - ";
                present = "Present";
            } else if (attType.equals("Clock-out") || attType.equals("")) {
                dateIn = " - ";
                timeIn = " - ";
                timeOut = rasLoader.getSheet().getCell(7, i).getContents();
                present = "Absent";
            }

            EmpExtPersistence.add(rasLoader.getSheet().getCell(6, i).getContents(),
                    rasLoader.getSheet().getCell(2, i).getContents(),
                    rasLoader.getSheet().getCell(1, i).getContents(),
                    rasLoader.getSheet().getCell(5, i).getContents(),
                    "0", attType,
                    dateIn, timeIn, timeOut,
                    present, timeIn, "Loan", "Advance", timeIn);
            SalaryInfo.add(rasLoader.getSheet().getCell(1, i).getContents(), "0");

            String empDataName = rasLoader.getSheet().getCell(2, i).getContents();
            String empDataID = rasLoader.getSheet().getCell(1, i).getContents();
            String finalName = " - ";
            String finalID = " - ";
            int empDataDays = 1;

            for (int x = 0; x < EmployeeDataPersistence.employeeDataList.size(); x++) {
                if ((empDataName.equals(EmployeeDataPersistence.employeeDataList.get(x).getName()))
                        && (empDataID.equals(EmployeeDataPersistence.employeeDataList.get(x).getId()))) {
                    empDataDays += 1;
                    finalName = EmployeeDataPersistence.employeeDataList.get(x).getName();
                    finalID = EmployeeDataPersistence.employeeDataList.get(x).getId();
                } else if (!(empDataName.equals(EmployeeDataPersistence.employeeDataList.get(x).getName()))) {
                    finalName = empDataName;
                    finalID = empDataID;
                    break;
                }
            }

            EmployeeDataPersistence.add(finalName, finalID, " - ", " - ", " - ", " - ", " - ", " - ",
                    empDataDays + "");

            String id = rasLoader.getSheet().getCell(1, i).getContents();
            String name = rasLoader.getSheet().getCell(2, i).getContents();
            String salary = "0";
            String location = rasLoader.getSheet().getCell(5, i).getContents();
            String attendType = rasLoader.getSheet().getCell(8, i).getContents();
            String arrivalTime = "08:30";
            String tax = "0";
            String loan = "0";
            String days = "1";
            String inTime = null;
            String outTime = null;

            if ((attendType.equals("Clock-out")) ) {
                inTime = rasLoader.getSheet().getCell(7, i - 1).getContents();
                outTime = rasLoader.getSheet().getCell(7, i).getContents();
            } 
                

            PersonPersistence.add(id, name, salary, location, attendType, arrivalTime,
                    tax, loan, days, inTime, outTime, true);
        }
        //EmpPersistence.load();
        //EmpExtPersistence.load();
    }

    private void initUIComps() {
        this.setLocationRelativeTo(null);
        this.jTextField1.setFont(new Font("Tahoma", Font.BOLD, 10));
        this.jTextField1.setEditable(false);
        this.jTextField1.setText("Log");
        logField = this.jTextField1;
    }

    private void makeImsTable() {
        imsTableModel = new DefaultTableModel();
        this.jLabel2.setText("Person Details");
        this.jLabel2.updateUI();
        //Setting table rows
        for (int i = 0; i < PersonPersistence.pInfoList.size(); i++) {
            String data[] = new String[15];
            imsTableModel.addRow(data);
        }

        imsTableModel.addColumn("ID");
        imsTableModel.addColumn("Name");
        imsTableModel.addColumn("Arrived At");
        imsTableModel.addColumn("Departed At");
        imsTableModel.addColumn("Salary");
        imsTableModel.addColumn("Tax");
        imsTableModel.addColumn("Advance");
        imsTableModel.addColumn("Total Salary");

        for (int i = 0; i < PersonPersistence.pInfoList.size(); i++) {
            imsTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getId(), i, 0);
            imsTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getName(), i, 1);
            imsTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getInTime(), i, 2);
            imsTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getOutTime(), i, 3);
            imsTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getSalary(), i, 4);
            imsTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getTax(), i, 5);
            imsTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getLoan(), i, 6);
            imsTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getFinalSalary(), i, 7);
        }

        imsTable = new JTable();
        imsTable.setModel(imsTableModel);
        this.jScrollPane1.setViewportView(imsTable);
        this.jScrollPane1.updateUI();

        imsTable = new JTable();
        imsTable.setModel(imsTableModel);
        this.jScrollPane1.setViewportView(imsTable);
        this.jScrollPane1.updateUI();
    }

    private void makeRasTable() {
        rasTableModel = new DefaultTableModel();
        this.jLabel2.setText("Person Info");
        this.jLabel2.updateUI();
        //Setting table rows
        for (int i = 0; i < PersonPersistence.pInfoList.size(); i++) {
            String data[] = new String[15];
            rasTableModel.addRow(data);
        }

        rasTableModel.addColumn("ID");
        rasTableModel.addColumn("Name");
        rasTableModel.addColumn("Place");
        rasTableModel.addColumn("Attendence");
        rasTableModel.addColumn("Days");
        rasTableModel.addColumn("In-Time");
        rasTableModel.addColumn("Hours");
        rasTableModel.addColumn("Arrived At");
        rasTableModel.addColumn("Departed At");

        for (int i = 0; i < PersonPersistence.pInfoList.size(); i++) {
            rasTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getId(), i, 0);
            rasTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getName(), i, 1);
            rasTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getLocation(), i, 2);
            rasTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getAttendence(), i, 3);
            rasTableModel.setValueAt(PersonPersistence.countDays(PersonPersistence.pInfoList.get(i).getName()), i, 4);
            rasTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getLate(), i, 5);
            rasTableModel.setValueAt(PersonPersistence.countHours(PersonPersistence.pInfoList.get(i).getName()), i, 6);
            rasTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getInTime(), i, 7);
            rasTableModel.setValueAt(PersonPersistence.pInfoList.get(i).getOutTime(), i, 8);
        }

        rasTable = new JTable();
        rasTable.setModel(rasTableModel);
        this.jScrollPane1.setViewportView(rasTable);
        this.jScrollPane1.updateUI();
    }

    private void makeEnumTable() {
        enumTableModel = new DefaultTableModel();
        this.jLabel2.setText("Enumerated");
        this.jLabel2.updateUI();

        for (int i = 0; i < EmpPersistence.employeeList.size(); i++) {
            String data[] = new String[6];
            enumTableModel.addRow(data);
        }

        //col name from the list
        enumTableModel.addColumn("ID");
        enumTableModel.addColumn("Name");
        enumTableModel.addColumn("Date");
        enumTableModel.addColumn("Place");
        enumTableModel.addColumn("Salary");
        enumTableModel.addColumn("Attendence Type");

        enumTableModel.addColumn("Date-In");
        enumTableModel.addColumn("Time-In");
        enumTableModel.addColumn("Time-Out");
        enumTableModel.addColumn("Attendence");
        enumTableModel.addColumn("Is-Late");
        enumTableModel.addColumn("Advance/Loan");
        enumTableModel.addColumn("Arrival-Time");

        for (int i = 0; i < EmpPersistence.employeeList.size(); i++) {
            enumTableModel.setValueAt(EmpPersistence.employeeList.get(i).getId(), i, 0);
            enumTableModel.setValueAt(EmpPersistence.employeeList.get(i).getName(), i, 1);
            enumTableModel.setValueAt(EmpPersistence.employeeList.get(i).getDate(), i, 2);
            enumTableModel.setValueAt(EmpPersistence.employeeList.get(i).getLocation(), i, 3);
            enumTableModel.setValueAt(EmpPersistence.employeeList.get(i).getSalary(), i, 4);
            enumTableModel.setValueAt(EmpPersistence.employeeList.get(i).getAttType(), i, 5);
        }

        for (int i = 0; i < EmpExtPersistence.empExtList.size(); i++) {
            enumTableModel.setValueAt(EmpExtPersistence.empExtList.get(i).getDateIn(), i, 6);
            enumTableModel.setValueAt(EmpExtPersistence.empExtList.get(i).getTimeIn(), i, 7);
            enumTableModel.setValueAt(EmpExtPersistence.empExtList.get(i).getTimeOut(), i, 8);
            enumTableModel.setValueAt(EmpExtPersistence.empExtList.get(i).getPresent(), i, 9);
            enumTableModel.setValueAt(EmpExtPersistence.empExtList.get(i).getLate(), i, 10);
            enumTableModel.setValueAt(EmpExtPersistence.empExtList.get(i).getAdvance(), i, 11);
            enumTableModel.setValueAt(EmpExtPersistence.empExtList.get(i).getArrivalTime(), i, 12);
        }

        enumTable = new JTable();
        enumTable.setModel(enumTableModel);
        this.jScrollPane1.setViewportView(enumTable);
        this.jScrollPane1.updateUI();
    }

    private void saveDataFromTable() {
        /**
         * *
         * try { for (int i = 1; i < empTable.getRowCount(); i++) {
         * EmpExtPersistence.add( empTable.getValueAt(i - 1, 0).toString(),
         * empTable.getValueAt(i - 1, 1).toString(), empTable.getValueAt(i - 1,
         * 2).toString(), empTable.getValueAt(i - 1, 3).toString(),
         * empTable.getValueAt(i - 1, 4).toString(), empTable.getValueAt(i - 1,
         * 5).toString(), empTable.getValueAt(i - 1, 6).toString(),
         * empTable.getValueAt(i - 1, 7).toString(), empTable.getValueAt(i - 1,
         * 8).toString(), empTable.getValueAt(i - 1, 9).toString(),
         * empTable.getValueAt(i - 1, 10).toString(), "", empTable.getValueAt(i
         * - 1, 11).toString(), empTable.getValueAt(i - 1, 12).toString()); } }
         * catch (Exception e) {
         * Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE,
         * "Saving data exception in EMPEXT: " + e.toString()); } *
         */
        EmpExtPersistence.write(EMP_EXT_FILENAME);
    }

    /**
     *
     * @param xlsInstead boolean
     */
    private void makeEmpTable(boolean xlsInstead) {
        empModel = new DefaultTableModel();
        this.jLabel2.setText("Employee");
        this.jLabel2.updateUI();
        //Setting table rows
        for (int i = 0; i < EmpPersistence.employeeList.size(); i++) {
            String data[] = new String[13];
            empModel.addRow(data);
        }

        //get Column name from the spread sheet and set table's cols
        empModel.addColumn("Name");
        empModel.addColumn("ID");
        empModel.addColumn("Salary");
        empModel.addColumn("Tax");
        empModel.addColumn("Advance/Loan");
        empModel.addColumn("Days");
        empModel.addColumn("Arrived");

        String names = null;
        String ID = null;
        String salary = null;
        String tax = null;
        String loan = null;
        String days = null;
        String arrivalTime = null;

        //get cell contents and put them in the table
        if (xlsInstead) {
            for (int i = 1; i < EmployeeDataPersistence.employeeDataList.size(); i++) {
                names = EmployeeDataPersistence.employeeDataList.get(i).getName();
                ID = EmployeeDataPersistence.employeeDataList.get(i).getId();
                salary = EmployeeDataPersistence.employeeDataList.get(i).getSalary();
                tax = EmployeeDataPersistence.employeeDataList.get(i).getTax();
                loan = EmployeeDataPersistence.employeeDataList.get(i).getLoan();
                days = EmployeeDataPersistence.employeeDataList.get(i).getDays();
                arrivalTime = EmployeeDataPersistence.employeeDataList.get(i).getArrivalTime();

                empModel.setValueAt(names, i - 1, 0);
                empModel.setValueAt(ID, i - 1, 1);
                empModel.setValueAt(salary, i - 1, 2);
                empModel.setValueAt(tax, i - 1, 3);
                empModel.setValueAt(loan, i - 1, 4);
                empModel.setValueAt(days, i - 1, 5);
                empModel.setValueAt(arrivalTime, i - 1, 6);
            }
        } else if (!xlsInstead) {
            try {
                workLoader = WorkbookLoader.getInstance();
                workLoader.loadWorkbook();
            } catch (IOException | BiffException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }

            empModel = new DefaultTableModel();
            this.jLabel2.setText("Employees");
            this.jLabel2.updateUI();
            //Setting table rows
            for (int i = 0; i < workLoader.getRowNos() - 1; i++) {
                String data[] = new String[workLoader.getColNos()];
                empModel.addRow(data);
            }

            //get Column name from the spread sheet and set table's cols
            for (int i = 0; i < workLoader.getColNos(); i++) {
                Cell[] column = workLoader.getLoadingSheet().getColumn(i);
                if (column.length > 0) {
                    if (!column[0].getContents().equals("")) {
                        String columnName = column[0].getContents();
                        empModel.addColumn(columnName);
                    }
                }
            }

            //get cell contents and put them in the table
            for (int i = 1; i < workLoader.getRowNos(); i++) {
                for (int j = 0; j < workLoader.getColNos(); j++) {
                    Cell cell = workLoader.getLoadingSheet().getCell(j, i);
                    empModel.setValueAt(cell.getContents(), i - 1, j);
                }
            }
        }

        empTable = new JTable();
        empTable.setModel(empModel);
        this.jScrollPane1.setViewportView(empTable);
        this.jScrollPane1.updateUI();
    }

    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                sleep(500);
                setProgress(50, "Done");
                Log.log(MainWindow.class.getName(), "Still Loading backend process");
                sleep(1000);
                setProgress(70, "Done");
                Log.log(MainWindow.class.getName(), "Almost done");
                sleep(800);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                Log.log(MainWindow.class.getName(), "Loader Thread Exception");
            } finally {
                if (loadAgain) {
                    loadEmpData();
                }
                setProgress(100, "Done");
                Log.log(MainWindow.class.getName(), "All files loaded successfully");
            }
        }

    };

    /**
     *
     * @param clazz
     * @param msg
     */
    public static void setLog(String clazz, String msg) {
        logField.setText(clazz + " -> " + msg + "...");
    }

    private void makeExitDialog() {
        Log.log(MainWindow.class.getName(), "Dispaying Exit Dialog");
        int response = JOptionPane.showConfirmDialog(this.rootPane, "Do you really want to exit?\n"
                + "All unsaved data will be saved automatically.", "ExitDialog", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, icon);

        writeCache();

        if (response == JOptionPane.YES_OPTION) {
            saveAll();
            System.exit(0);
        } else if (response == JOptionPane.NO_OPTION) {
            Log.log(MainWindow.class.getName(), "Exit Dialog closed");
        }
    }

    private void saveAll() {
        EmpPersistence.write(EMP_LIST_FILENAME);
        SalaryInfo.write(SLR_FILENAME);
        saveDataFromTable();
        AppConfPersistence.write(CONF_FILE_NAME);
        try {
            PersonPersistence.save(FILE_PERSON_INFO);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param val
     * @param msg
     */
    private void setProgress(int val, String msg) {
        this.jProgressBar1.setValue(val);
        this.jProgressBar1.setStringPainted(true);
        if (jProgressBar1.getValue() == 100) {
            this.jProgressBar1.setString("Done");
        }
    }

    private void makeAboutDialog() {
        AboutDialog abt = new AboutDialog(this, true);
        abt.setLocationRelativeTo(null);
        abt.pack();
        abt.setVisible(true);
    }

    private void makePrefsDialog() {
        PrefsDialog dia = new PrefsDialog(this, true);
        dia.pack();
        dia.setLocationRelativeTo(null);
        dia.setVisible(true);
    }

    public void finish() {
        setVisible(false);
        dispose();
    }

    private void makeSorryDialog() {
        JOptionPane.showMessageDialog(this, "Sorry, this action is not supported in this version!.", "Usupported Dialog",
                JOptionPane.WARNING_MESSAGE,
                ImageIconFactory.makeImageIcon("gfx/about_icon.png", "About", LoginDialog.class));
    }

    public static void changeLAF(String ui) {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (ui.equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(PrefsDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (ui.equals("System")) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jSeparator5 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox();
        jSeparator7 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Pioneer XLS Interpretor by Algorithmi [v-1.0.5b] Linux/Unix/Mac/MS-Windows");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), new java.awt.Color(102, 102, 102)));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 153, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_download.png"))); // NOI18N
        jLabel1.setText("Contact Developer");
        jLabel1.setToolTipText("DEVELOPER WEB=http://1-dot-muneeb-ahmad.appspot.com/controller?nav=connect");

        jProgressBar1.setToolTipText("Progress of any process or thread, if running any!");

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Person Info", "Persons", "Employee", "Enumerated" }));
        jComboBox1.setToolTipText("Select table view");
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(326, 326, 326)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator5)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 5, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
            .addComponent(jSeparator7)
        );

        jScrollPane1.setBackground(new java.awt.Color(102, 102, 102));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jTextField1.setBackground(new java.awt.Color(102, 102, 102));
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setToolTipText("Log view");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_info.png"))); // NOI18N
        jButton1.setToolTipText("About application");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_run.png"))); // NOI18N
        jButton2.setToolTipText("Preferences of application");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel2.setToolTipText("Name of Loaded Sheet");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_edit.png"))); // NOI18N
        jButton3.setText("Edit");
        jButton3.setToolTipText("Edit persons and person info items only");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/jtree_menu.png"))); // NOI18N
        jMenu1.setText("Application");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_info.png"))); // NOI18N
        jMenuItem1.setText("About");
        jMenuItem1.setToolTipText("About this application");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_run.png"))); // NOI18N
        jMenuItem2.setText("Preferences...");
        jMenuItem2.setToolTipText("Preferences of application");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_ok.png"))); // NOI18N
        jMenuItem3.setText("Exit Application");
        jMenuItem3.setToolTipText("Exit this application.");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_file.png"))); // NOI18N
        jMenu2.setText("File");
        jMenu2.setToolTipText("File menu");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_panel.png"))); // NOI18N
        jMenuItem4.setText("New");
        jMenuItem4.setToolTipText("Create new employee");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_folder.png"))); // NOI18N
        jMenuItem5.setText("Open");
        jMenuItem5.setToolTipText("Open from directory");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);
        jMenu2.add(jSeparator6);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/contact_image.png"))); // NOI18N
        jMenuItem6.setText("Save Employees");
        jMenuItem6.setToolTipText("Save Employees before exiting this app");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);
        jMenu2.add(jSeparator1);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/save_ic.png"))); // NOI18N
        jMenuItem7.setText("Save All");
        jMenuItem7.setToolTipText("Save all");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk/muneebahmad/client/pioneer/ui/gfx/ic_download_stable.png"))); // NOI18N
        jMenuItem8.setText("Payroll");
        jMenuItem8.setToolTipText("Payroll");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator4))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        makeAboutDialog();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        makeExitDialog();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        //SAVE ALL
        saveAll();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        makeAboutDialog();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        makePrefsDialog();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        makePrefsDialog();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        createEmployee();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        makeSorryDialog();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        writeExcel();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (this.jComboBox1.getSelectedItem().equals("Person Info")) {
            this.jButton3.setEnabled(true);
            this.jMenuItem6.setEnabled(false);
            makeImsTable();
        } else if (this.jComboBox1.getSelectedItem().equals("Persons")) {
            this.jButton3.setEnabled(true);
            this.jMenuItem6.setEnabled(false);
            makeRasTable();
        } else if (this.jComboBox1.getSelectedItem().equals("Enumerated")) {
            this.jButton3.setEnabled(false);
            this.jMenuItem6.setEnabled(false);
            makeEnumTable();
        } else if (this.jComboBox1.getSelectedItem().equals("Employee")) {
            this.jButton3.setEnabled(false);
            this.jMenuItem6.setEnabled(true);
            File inputXLS = new File(WORKBOOK_FILLENAME);
            if (inputXLS.canRead()) {
                makeEmpTable(false);
            } else {
                makeEmpTable(true);
            }
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        EditDialog dia = new EditDialog(this, true);
        dia.setLocationRelativeTo(null);
        dia.pack();
        dia.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        PayrollForm dis = new PayrollForm(this, true);
        dis.setLocationRelativeTo(null);
        dis.pack();
        dis.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    private void createEmployee() {
        if (this.jComboBox1.getSelectedItem().equals("Employee") || this.jComboBox1.getSelectedItem().equals("Enumerated")) { 
            EmployeeForm dia = new EmployeeForm(this, true);
            dia.setLocationRelativeTo(null);
            dia.pack();
            dia.setVisible(true);
        } else if (jComboBox1.getSelectedItem().equals("Person Info") || this.jComboBox1.getSelectedItem().equals("Persons")) {
            PersonsForm eis = new PersonsForm(this, true);
            eis.setLocationRelativeTo(null);
            eis.pack();
            eis.setVisible(true);
        }
    }

    private void writeExcel() {
        WorkbookLoader ldr = WorkbookLoader.getInstance();
        ldr.prepareWorkbookForWriting();
        try {
            ldr.writeTitle(0, 0, "Name");
            ldr.writeTitle(1, 0, "ID");
            ldr.writeTitle(2, 0, "Salary");
            ldr.writeTitle(3, 0, "Tax");
            ldr.writeTitle(4, 0, "Advance/Loan");
            ldr.writeTitle(5, 0, "Days");
            ldr.writeTitle(6, 0, "Arrived");

            for (int i = 0; i < empTable.getRowCount(); i++) {
                for (int j = 0; j < empTable.getColumnCount(); j++) {
                    ldr.writeTitle(j, i + 1, empTable.getValueAt(i, j).toString());
                }
            }
            ldr.persist();
        } catch (WriteException | IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
/**
 * end class.
 */
