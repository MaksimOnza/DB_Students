package students.frame;

import java.text.DateFormat;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import students.logic.Student;

public class StudentTableModel extends AbstractTableModel {

    private Vector students;

    public StudentTableModel(Vector students) {
        this.students = students;
    }

    public int getRowCount() {
        if (students != null) {
            return students.size();
        }
        return 0;
    }

    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int column) {
        String[] colNames = {"Фамилия", "Имя", "Отчество", "Дата"};
        return colNames[column];
    }

    // Возвращаем данные для определенной строки и столбца
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (students != null) {
            Student st = (Student) students.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return st.getSurName();
                case 1:
                    return st.getFirstName();
                case 2:
                    return st.getPatronymic();
                case 3:
                    return DateFormat.getDateInstance(DateFormat.SHORT).format(
                            st.getDateOfBirth());
            }
        }
        return null;
    }

    // Метод, который возвращает студента по номеру строки
    public Student getStudent(int rowIndex) {
        if (students != null) {
            if (rowIndex < students.size() && rowIndex >= 0) {
                return (Student) students.get(rowIndex);
            }
        }
        return null;
    }
}
