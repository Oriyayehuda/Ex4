## classes:
#### Ex2GUI.java:
Handles the graphical interface for the spreadsheet, allowing users to view, edit, and save data.
#### Ex2Utils.java:
Stores fixed values like spreadsheet size, colors, and math functions.
#### StdDrawEx2.java:
Provides drawing tools for lines, shapes, and text in the spreadsheet.
### cell.java:
This interface represents a cell in a spreadsheet and lets you get or set its data, type (like text, number, or formula), and calculation order.
### cellEntry.java:
This class represents a spreadsheet cell entry by converting its string address into row and column numbers and checking if it’s valid.
### Ex2Sheet.java:
This class checks and calculates different types of data like numbers, formulas, functions, and conditions.
It looks for valid expressions and computes results using basic operations like add, subtract, multiply, and divide.
It also handles special functions like finding the minimum, maximum, sum, and average of cell ranges.
### Index2D.java:
