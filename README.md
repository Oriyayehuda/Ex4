## classes:
#### Ex2GUI.java:
Handles the graphical interface for the spreadsheet, allowing users to view, edit, and save data.
#### Ex2Utils.java:
Stores fixed values like spreadsheet size, colors, and math functions.
#### StdDrawEx2.java:
Provides drawing tools for lines, shapes, and text in the spreadsheet.
#### cell.java:
This interface represents a cell in a spreadsheet and lets you get or set its data, type (like text, number, or formula), and calculation order.
#### cellEntry.java:
This class represents a spreadsheet cell entry by converting its string address into row and column numbers and checking if it’s valid.
#### Ex2Sheet.java:
This class checks and calculates different types of data like numbers, formulas, functions, and conditions.
It looks for valid expressions and computes results using basic operations like add, subtract, multiply, and divide.
It also handles special functions like finding the minimum, maximum, sum, and average of cell ranges.
#### Index2D.java:
The class checks and calculates formulas, functions, and conditions in a spreadsheet.
It makes sure they are written correctly and finds their values when needed.
#### sheet.java:
This class represents a spreadsheet that stores and manages a 2D table of cells.
It allows setting and getting values, calculating cell values, tracking dependencies, and saving or loading data from a file.
#### Scell:
The class represents a cell that stores data as text and sorts it into types like 
text, number, formula, or function. It also keeps track of the cell's order and type.
#### Range2D:
The class represents a range of cells from a starting cell to an ending cell.
It can return all the cell names in that range.


<img width="890" alt="readme photo" src="https://github.com/user-attachments/assets/414b139d-6af8-4ce7-9dba-676bab7bd155" />
















