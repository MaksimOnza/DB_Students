package students.frame;

import java.sql.SQLException;
import java.util.Vector;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.Student;

public class StudentsFrame extends JFrame implements ActionListener, ListSelectionListener, ChangeListener {
    // Имена для кнопок

    private static final String MOVE_GR = "moveGroup";
    private static final String CLEAR_GR = "clearGroup";
    private static final String INSERT_ST = "insertStudent";
    private static final String UPDATE_ST = "updateStudent";
    private static final String DELETE_ST = "deleteStudent";
    private static final String ALL_STUDENTS = "allStudent";
    private ManagementSystem ms = null;
    private JList grpList;
    private JTable stdList;
    private JSpinner spYear;

    public StudentsFrame() throws Exception {
        // layout для всей клиентской части формы
        getContentPane().setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Отчеты");
        JMenuItem menuItem = new JMenuItem("Все студенты");
        menuItem.setName(ALL_STUDENTS);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Верхняя панель
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT));

        top.add(new JLabel("Год обучения:"));
        SpinnerModel sm = new SpinnerNumberModel(2006, 1900, 2100, 1);
        spYear = new JSpinner(sm);
        spYear.addChangeListener(this);
        top.add(spYear);

        // Нижняя панель
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());

        // Левая панель
        GroupPanel left = new GroupPanel();
        left.setLayout(new BorderLayout());
        left.setBorder(new BevelBorder(BevelBorder.LOWERED));

        ms = ManagementSystem.getInstance();
        Vector<Group> gr = new Vector<Group>(ms.getGroups());
        left.add(new JLabel("Группы:"), BorderLayout.NORTH);
        grpList = new JList(gr);
        grpList.addListSelectionListener(this);
        grpList.setSelectedIndex(0);
        left.add(new JScrollPane(grpList), BorderLayout.CENTER);
        JButton btnMvGr = new JButton("Переместить");
        btnMvGr.setName(MOVE_GR);
        JButton btnClGr = new JButton("Очистить");
        btnClGr.setName(CLEAR_GR);
        btnMvGr.addActionListener(this);
        btnClGr.addActionListener(this);
        // Панель для кнопок
        JPanel pnlBtnGr = new JPanel();
        pnlBtnGr.setLayout(new GridLayout(1, 2));
        pnlBtnGr.add(btnMvGr);
        pnlBtnGr.add(btnClGr);
        left.add(pnlBtnGr, BorderLayout.SOUTH);

        // Правая панель
        JPanel right = new JPanel();
        right.setLayout(new BorderLayout());
        right.setBorder(new BevelBorder(BevelBorder.LOWERED));

        right.add(new JLabel("Ñòóäåíòû:"), BorderLayout.NORTH);
        // Создаем таблицу
        stdList = new JTable(1, 4);
        right.add(new JScrollPane(stdList), BorderLayout.CENTER);
        // Кнопки для студентов
        JButton btnAddSt = new JButton("Добавить");
        btnAddSt.setName(INSERT_ST);
        btnAddSt.addActionListener(this);
        JButton btnUpdSt = new JButton("Исправить");
        btnUpdSt.setName(UPDATE_ST);
        btnUpdSt.addActionListener(this);
        JButton btnDelSt = new JButton("Удалить");
        btnDelSt.setName(DELETE_ST);
        btnDelSt.addActionListener(this);
        // Панель для кнопок
        JPanel pnlBtnSt = new JPanel();
        pnlBtnSt.setLayout(new GridLayout(1, 3));
        pnlBtnSt.add(btnAddSt);
        pnlBtnSt.add(btnUpdSt);
        pnlBtnSt.add(btnDelSt);
        right.add(pnlBtnSt, BorderLayout.SOUTH);

        bot.add(left, BorderLayout.WEST);
        bot.add(right, BorderLayout.CENTER);

        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(bot, BorderLayout.CENTER);

        // границы формы
        setBounds(100, 100, 700, 500);
    }

    // Метод для обеспечения интерфейса ActionListener
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component c = (Component) e.getSource();
            if (c.getName().equals(MOVE_GR)) {
                moveGroup();
            }
            if (c.getName().equals(CLEAR_GR)) {
                clearGroup();
            }
            if (c.getName().equals(ALL_STUDENTS)) {
                showAllStudents();
            }
            if (c.getName().equals(INSERT_ST)) {
                insertStudent();
            }
            if (c.getName().equals(UPDATE_ST)) {
                updateStudent();
            }
            if (c.getName().equals(DELETE_ST)) {
                deleteStudent();
            }
        }
    }

    // Метод для обеспечения интерфейса ListSelectionListener
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            reloadStudents();
        }
    }

    // Метод для обеспечения интерфейса ChangeListener
    public void stateChanged(ChangeEvent e) {
        reloadStudents();
    }

    // обновление списка студентов для определенной группы
    public void reloadStudents() {
	    // Создаем анонимный класс для потока
        Thread t = new Thread() {

            public void run() {
                if (stdList != null) {
                    Group g = (Group) grpList.getSelectedValue();
                    int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                    try {
                        Collection<Student> s = ms.getStudentsFromGroup(g, y);
                        stdList.setModel(new StudentTableModel(new Vector<Student>(s)));
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                    }
                }
            }
        };
        t.start();
    }

    // метод для переноса группы
    private void moveGroup() {
        Thread t = new Thread() {

            public void run() {
                if (grpList.getSelectedValue() == null) {
                    return;
                }
                try {
                    Group g = (Group) grpList.getSelectedValue();
                    int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                    // Ñîçäàåì íàø äèàëîã
                    GroupDialog gd = new GroupDialog(y, ms.getGroups());
                    gd.setModal(true);
                    gd.setVisible(true);
                    if (gd.getResult()) {
                        ms.moveStudentsToGroup(g, y, gd.getGroup(), gd.getYear());
                        reloadStudents();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                }
            }
        };
        t.start();
    }

    // метод для очистки группы
    private void clearGroup() {
        Thread t = new Thread() {

            public void run() {
                if (grpList.getSelectedValue() != null) {
                    if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        Group g = (Group) grpList.getSelectedValue();
                        int y = ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
                        try {
                            ms.removeStudentsFromGroup(g, y);
                            reloadStudents();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                        }
                    }
                }
            }
        };
        t.start();
    }

    // метод для добавления студента
    private void insertStudent() {
        Thread t = new Thread() {

            public void run() {
                try {
                    StudentDialog sd = new StudentDialog(ms.getGroups(), true, StudentsFrame.this);
                    sd.setModal(true);
                    sd.setVisible(true);
                    if (sd.getResult()) {
                        Student s = sd.getStudent();
                        ms.insertStudent(s);
                        reloadStudents();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                }
            }
        };
        t.start();
    }

    // метод для редактирования студента
    private void updateStudent() {
        Thread t = new Thread() {

            public void run() {
                if (stdList != null) {
                    StudentTableModel stm = (StudentTableModel) stdList.getModel();
                    if (stdList.getSelectedRow() >= 0) {
                        Student s = stm.getStudent(stdList.getSelectedRow());
                        try {
                            StudentDialog sd = new StudentDialog(ms.getGroups(), false, StudentsFrame.this);
                            sd.setStudent(s);
                            sd.setModal(true);
                            sd.setVisible(true);
                            if (sd.getResult()) {
                                Student us = sd.getStudent();
                                ms.updateStudent(us);
                                reloadStudents();
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                        }
                    } 
                    else {
                        JOptionPane.showMessageDialog(StudentsFrame.this,
                                "Необходимо выделить студента в списке");
                    }
                }
            }
        };
        t.start();
    }

    // метод для удаления студента
    private void deleteStudent() {
        Thread t = new Thread() {

            public void run() {
                if (stdList != null) {
                    StudentTableModel stm = (StudentTableModel) stdList.getModel();
                    if (stdList.getSelectedRow() >= 0) {
                        if (JOptionPane.showConfirmDialog(StudentsFrame.this,
                                "Вы хотите удалить студента?", "Удаление студента",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            Student s = stm.getStudent(stdList.getSelectedRow());
                            try {
                                ms.deleteStudent(s);
                                reloadStudents();
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(StudentsFrame.this, e.getMessage());
                            }
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(StudentsFrame.this, "Необходимо выделить студента в списке");
                    }
                }
            }
        };
        t.start();
    }

    // метод для показа всех студентов
    private void showAllStudents() {
        //JOptionPane.showMessageDialog(this, "showAllStudents");
    	//----------------------------
    	Collection<Student> s;
		try {
			s = ms.getAllStudents();
        stdList.setModel(new StudentTableModel(new Vector<Student>(s)));
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    // Ìû ñðàçó îòìåíèì ïðîäîëæåíèå ðàáîòû, åñëè íå ñìîæåì ïîëó÷èòü
                    // êîííåêò ê áàçå äàííûõ
                    StudentsFrame sf = new StudentsFrame();
                    sf.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    sf.setVisible(true);
                    sf.reloadStudents();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }
}

// переопределенная панель
class GroupPanel extends JPanel {

    public Dimension getPreferredSize() {
        return new Dimension(250, 0);
    }
}
