Test_main:
addi $t0, $zero, 0
add $a0, $zero, $t0
jal Test2_Start
add $t1, $zero, $v0
add $a0, $zero, $t1
jal _system_out_println
jal _system_exit

Test2_Start:
; FUNCTION PROLOGUE:
; Spill all general-purpose registers into RAM
addi $sp, $sp, -80 ; move the stack pointer back by 80 bytes (20 gp registers * 4 bytes each)
sw $ra, 0($sp)
sw $s0, 4($sp)
sw $s1, 8($sp)
sw $s2, 12($sp)
sw $s3, 16($sp)
sw $s4, 20($sp)
sw $s5, 24($sp)
sw $s6, 28($sp)
sw $s7, 32($sp)
sw $s8, 36($sp)
sw $t0, 40($sp)
sw $t1, 44($sp)
sw $t2, 48($sp)
sw $t3, 52($sp)
sw $t4, 56($sp)
sw $t5, 60($sp)
sw $t6, 64($sp)
sw $t7, 68($sp)
sw $t8, 72($sp)
sw $t9, 76($sp)
; FUNCTION BODY:
add $t0, $zero, $a0
addi $t1, $zero, 2
add $t2, $t0, $t1
addi $t3, $zero, 7
mult $t4, $t2, $t3
add $t5, $zero, $t4
add $v0, $zero, $t5
; FUNCTION Epilogue:
; Restore general-purpose registers from RAM:
lw $ra, 0($sp)
lw $s0, 4($sp)
lw $s1, 8($sp)
lw $s2, 12($sp)
lw $s3, 16($sp)
lw $s4, 20($sp)
lw $s5, 24($sp)
lw $s6, 28($sp)
lw $s7, 32($sp)
lw $s8, 36($sp)
lw $t0, 40($sp)
lw $t1, 44($sp)
lw $t2, 48($sp)
lw $t3, 52($sp)
lw $t4, 56($sp)
lw $t5, 60($sp)
lw $t6, 64($sp)
lw $t7, 68($sp)
lw $t8, 72($sp)
lw $t9, 76($sp)
addi $sp, $sp, 80 ; free the memory we used for spills
jr $ra
