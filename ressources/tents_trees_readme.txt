The first row and column indicate the number of tents per column/row.
All other fields are either empty or contain ‘t’, indicating a tree.
Values in the file are separated by comma (not semicolon).
Be aware that fields are *separated* by comma, which means the last field in each row does not "end" with a comma.
If you use the Java split() function for Strings on the lines of the CSV files add a limit of -1 to include trailing empty fields as such: split(",", -1)

small example:

,1,1,2,1,1,2,1
1,,,,t,,,
1,,t,,,,t,
2,,,,,,,
1,t,,,t,,,t
1,,,,,,,
1,,t,,,,t,
2,,,,t,,,

more visual representation of the above:

  1 1 2 1 1 2 1 
1       t       
1   t       t   
2               
1 t     t     t 
1               
1   t       t   
2       t       