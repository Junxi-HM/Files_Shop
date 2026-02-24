package view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import main.Shop;
import model.Product;

public class InventoryView extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;

    public InventoryView(Shop shop) {
        setTitle("Inventario Actual");
        setModal(true);
        setBounds(100, 100, 500, 400);
        getContentPane().setLayout(new BorderLayout());

        // Define table columns
        String[] columnNames = {"ID", "Nombre", "Precio", "Stock", "Disponible"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Load data from shop inventory
        ArrayList<Product> inventory = shop.getInventory();
        for (Product p : inventory) {
            Object[] row = {
                p.getId(),
                p.getName(),
                p.getWholesalerPrice() != null ? p.getWholesalerPrice().toString() : p.getPrice(),
                p.getStock(),
                p.isAvailable() ? "Sí" : "No"
            };
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}