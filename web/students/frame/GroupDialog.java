package students.frame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import students.logic.Group;

public class GroupDialog extends JDialog implements ActionListener {

    private static final int D_HEIGHT = 150;   // высота
    private final static int D_WIDTH = 200;   // ширина
    private JSpinner spYear;
    private JComboBox groupList;
    private JButton btnOk = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private boolean result = false;

    public GroupDialog(int year, List<Group> groups) {
        setTitle("Перенос группы");

        // layout для нашего окна
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        // Первый элемент
        JLabel l = new JLabel("Новая группа:");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        gbl.setConstraints(l, c);
        getContentPane().add(l);

        // Второй элемент
        groupList = new JComboBox(new Vector<Group>(groups));
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(groupList, c);
        getContentPane().add(groupList);

        // Третий элемент
        l = new JLabel("Новый год:");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        gbl.setConstraints(l, c);
        getContentPane().add(l);

        // увеличиваем группу на один год - для перевода
        spYear = new JSpinner(new SpinnerNumberModel(year + 1, 1900, 2100, 1));
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(spYear, c);
        getContentPane().add(spYear);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        btnOk.setName("OK");
        btnOk.addActionListener(this);
        gbl.setConstraints(btnOk, c);
        getContentPane().add(btnOk);

        btnCancel.setName("Cancel");
        btnCancel.addActionListener(this);
        gbl.setConstraints(btnCancel, c);
        getContentPane().add(btnCancel);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Получаем размеры экрана и вычисляя координаты на основе полученной информации помещаем его по центру
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(((int) d.getWidth() - GroupDialog.D_WIDTH) / 2, ((int) d.getHeight() - GroupDialog.D_HEIGHT) / 2,
                GroupDialog.D_WIDTH, GroupDialog.D_HEIGHT);
    }

    public int getYear() {
        return ((SpinnerNumberModel) spYear.getModel()).getNumber().intValue();
    }

    public Group getGroup() {
        if (groupList.getModel().getSize() > 0) {
            return (Group) groupList.getSelectedItem();
        }
        return null;
    }

    public boolean getResult() {
        return result;
    }

    // Обработка нжатия кнопок
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        if (src.getName().equals("OK")) {
            result = true;
        }
        if (src.getName().equals("Cancel")) {
            result = false;
        }
        setVisible(false);
    }
}
