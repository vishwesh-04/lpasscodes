START 100
L1 MOVER AREG X
MOVEM AREG Y
ORIGIN L1+3
NEXT ADD AREG X
SUB BREG Y
BC LT L1
ORIGIN NEXT+5
MULT CREG Z
STOP
X DS 2
Y DS 1
Z DC '9'
END