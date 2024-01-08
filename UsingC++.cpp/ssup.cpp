#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QLabel>
#include <QtWidgets/QPushButton>

int main(int argc, char *argv[]) {
    QApplication app(argc, argv);

    // Create the main window
    QMainWindow mainWindow;
    mainWindow.setWindowTitle("Blockchain GUI");
    mainWindow.setGeometry(100, 100, 400, 300);

    // Create layout
    QVBoxLayout *layout = new QVBoxLayout();

    // Create labels
    QLabel *titleLabel = new QLabel("Blockchain Information");
    titleLabel->setAlignment(Qt::AlignCenter);
    QLabel *blockInfoLabel = new QLabel("Block Information Here");

    // Create buttons
    QPushButton *viewButton = new QPushButton("View Blocks");
    QPushButton *addButton = new QPushButton("Add Block");

    // Add widgets to layout
    layout->addWidget(titleLabel);
    layout->addWidget(blockInfoLabel);
    layout->addWidget(viewButton);
    layout->addWidget(addButton);

    // Set layout for main window
    QWidget *centralWidget = new QWidget(&mainWindow);
    centralWidget->setLayout(layout);
    mainWindow.setCentralWidget(centralWidget);

    mainWindow.show();
    return app.exec();
}
