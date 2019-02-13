package students.frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.Student;

public class StudentDialog extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 200;   // âûñîòà îêíà
    private final static int D_WIDTH = 450;   // øèðèíà îêíà
    private final static int L_X = 10;      // левая граница метки для поля
    private final static int L_W = 100;     // ширина метки для поля
    private final static int C_W = 150;     // ширина поля
   
    private StudentsFrame owner;
   
    private boolean result = false;
    // Параметры студента
    private int studentId = 0;
    private JTextField firstName = new JTextField();
    private JTextField surName = new JTextField();
    private JTextField patronymic = new JTextField();
    private JSpinner dateOfBirth = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    private ButtonGroup sex = new ButtonGroup();
    private JSpinner year = new JSpinner(new SpinnerNumberModel(2006, 1900, 2100, 1));
    private JComboBox groupList;

    // newStudent - добавление студента или исправление
    public StudentDialog(List<Group> groups, boolean newStudent, StudentsFrame owner) {
        // Перегрузка списка через метод в главной форме
        this.owner = owner;
        setTitle("Редактирование данных студента");
        getContentPane().setLayout(new FlowLayout());

        groupList = new JComboBox(new Vector<Group>(groups));

        JRadioButton m = new JRadioButton("Муж");
        JRadioButton w = new JRadioButton("Жен");
        m.setActionCommand("М");
        w.setActionCommand("Ж");
        sex.add(m);
        sex.add(w);

        getContentPane().setLayout(null);

        // Разместим компоненты по абсолютным координатам
        JLabel l = new JLabel("Фамилия:", JLabel.RIGHT);
        l.setBounds(L_X, 10, L_W, 20);
        getContentPane().add(l);
        surName.setBounds(L_X + L_W + 10, 10, C_W, 20);
        getContentPane().add(surName);
        
        l = new JLabel("Имя:", JLabel.RIGHT);
        l.setBounds(L_X, 30, L_W, 20);
        getContentPane().add(l);
        firstName.setBounds(L_X + L_W + 10, 30, C_W, 20);
        getContentPane().add(firstName);
       
        l = new JLabel("Отчество:", JLabel.RIGHT);
        l.setBounds(L_X, 50, L_W, 20);
        getContentPane().add(l);
        patronymic.setBounds(L_X + L_W + 10, 50, C_W, 20);
        getContentPane().add(patronymic);
      
        l = new JLabel("Пол:", JLabel.RIGHT);
        l.setBounds(L_X, 70, L_W, 20);
        getContentPane().add(l);
        m.setBounds(L_X + L_W + 10, 70, C_W / 2, 20);
        getContentPane().add(m);
        
        // По умолчанию женщина
        w.setBounds(L_X + L_W + 10 + C_W / 2, 70, C_W / 2, 20);
        w.setSelected(true);
        getContentPane().add(w);

        l = new JLabel("Дата рождения:", JLabel.RIGHT);
        l.setBounds(L_X, 90, L_W, 20);
        getContentPane().add(l);
        dateOfBirth.setBounds(L_X + L_W + 10, 90, C_W, 20);
        getContentPane().add(dateOfBirth);
 
        l = new JLabel("Группа:", JLabel.RIGHT);
        l.setBounds(L_X, 115, L_W, 25);
        getContentPane().add(l);
        groupList.setBounds(L_X + L_W + 10, 115, C_W, 25);
        getContentPane().add(groupList);
    
        l = new JLabel("Год обучения:", JLabel.RIGHT);
        l.setBounds(L_X, 145, L_W, 20);
        getContentPane().add(l);
        year.setBounds(L_X + L_W + 10, 145, C_W, 20);
        getContentPane().add(year);

        JButton btnOk = new JButton("OK");
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        btnOk.setBounds(L_X + L_W + C_W + 10 + 50, 10, 100, 25);
        getContentPane().add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(L_X + L_W + C_W + 10 + 50, 40, 100, 25);
        getContentPane().add(btnCancel);

        if (newStudent) {
            JButton btnNew = new JButton("New");
            btnNew.setName("New");
            btnNew.addActionListener(this);
            btnNew.setBounds(L_X + L_W + C_W + 10 + 50, 70, 100, 25);
            getContentPane().add(btnNew);
        }

        // Устанавливаем поведение формы при закрытии - не закрывать совсем.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Получаем размеры экрана и вычисляя координаты на основе полученной информации помещаем его по центру.
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(((int) d.getWidth() - StudentDialog.D_WIDTH) / 2, ((int) d.getHeight() - StudentDialog.D_HEIGHT) / 2,
                StudentDialog.D_WIDTH, StudentDialog.D_HEIGHT);
    }


    public void setStudent(Student st) {
        studentId = st.getStudentId();
        firstName.setText(st.getFirstName());
        surName.setText(st.getSurName());
        patronymic.setText(st.getPatronymic());
        dateOfBirth.getModel().setValue(st.getDateOfBirth());
        for (Enumeration e = sex.getElements(); e.hasMoreElements();) {
            AbstractButton ab = (AbstractButton) e.nextElement();
            ab.setSelected(ab.getActionCommand().equals(new String("" + st.getSex())));
        }
        year.getModel().setValue(new Integer(st.getEducationYear()));
        for (int i = 0; i < groupList.getModel().getSize(); i++) {
            Group g = (Group) groupList.getModel().getElementAt(i);
            if (g.getGroupId() == st.getGroupId()) {
                groupList.setSelectedIndex(i);
                break;
            }
        }
    }

   
    public Student getStudent() {
        Student st = new Student();
        st.setStudentId(studentId);
        st.setFirstName(firstName.getText());
        st.setSurName(surName.getText());
        st.setPatronymic(patronymic.getText());
        Date d = ((SpinnerDateModel) dateOfBirth.getModel()).getDate();
        st.setDateOfBirth(d);
        for (Enumeration e = sex.getElements(); e.hasMoreElements();) {
            AbstractButton ab = (AbstractButton) e.nextElement();
            if (ab.isSelected()) {
                st.setSex(ab.getActionCommand().charAt(0));
            }
        }
        int y = ((SpinnerNumberModel) year.getModel()).getNumber().intValue();
        st.setEducationYear(y);
        st.setGroupId(((Group) groupList.getSelectedItem()).getGroupId());
        return st;
    }

 
    public boolean getResult() {
        return result;
    }

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        // Добавляем студента
        if (src.getName().equals("New")) {
            result = true;
            try {
                ManagementSystem.getInstance().insertStudent(getStudent());
                owner.reloadStudents();
                firstName.setText("");
                surName.setText("");
                patronymic.setText("");
            } catch (Exception sql_e) {
                JOptionPane.showMessageDialog(this, sql_e.getMessage());
            }
            return;
        }
        if (src.getName().equals("OK")) {
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}
