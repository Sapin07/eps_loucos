.data
msg_m: .asciiz "Digite m: "
msg_n: .asciiz "Digite n: "
msg_mmc: .asciiz "O MMC é: "
nl: .asciiz "\n"
m: .word 0
n: .word 0


.text
.globl main

main:
    	# Imprime string
    	li $v0, 4
    	la $a0, msg_m
    	syscall

    	# Le inteiro m
    	li $v0, 5
    	syscall
    	sw $v0, m

    	# Imprime string
    	li $v0, 4
    	la $a0, msg_n
    	syscall

    	# Ler inteiro n
    	li $v0, 5
    	syscall
    	sw $v0, n

    	li $s0, 1 # $s0 = mmc = 1
    	li $s1, 2 # $s1 = d = 2
    
    	# vai para mmc

# --------------------------------------------------------------------------
# AS 4 "SESSOES" A SEGUIR SERIA CORRESPONDENTE A FUCAO MMC (DO ITEM 'B')

mmc:
    	# carrega m e n
    	lw $t0, m # $t0 = m
    	lw $t1, n # $t1 = n

    	# $t2 = minimoMMC = 1
    	li $t2, 1
    
    	# se m!=1 continua tentando dividir
    	bne $t0, $t2, tentaDividir
    
    	# A essa ponto do codigo pelo menos m eh 1
    	# mas talvez n ainda pode ser divisivel
    
    	# se n!=1 continua tentando dividir
    	bne $t1, $t2, tentaDividir
    	
    	j imprime

tentaDividir:
    	la $a0, m          # $a0 = &m
    	la $a1, n          # $a1 = &n 
    	move $a2, $s1        # $a2 = d

    	jal divide

    	beqz $v0, nao_dividiu # se não dividiu ninguém, tenta outro d.

   		mul $s0, $s0, $s1 # caso contrário, pelo menos um deles foi dividido. Multiplicar o MMC pelo d atual.
    	j mmc

nao_dividiu:
    	addi $s1, $s1, 1 # d++
    	j mmc

imprime:
		# imprime string de msg MMC
    	li $v0, 4
    	la $a0, msg_mmc
    	syscall
	
		# imprime string do MMC
    	li $v0, 1
    	move $a0, $s0
    	syscall
	
		# acaba o programa ;D
    	li $v0, 10
    	syscall

# --------------------------------------------------------------------------
# AS 3 "SESSOES" A SEGUIR SERIA CORRESPONDENTE A FUNCAO DIVIDE (DO ITEM 'A')

divide:
		li $v0, 0 # retorno eh zero ate que se prove o contrario
		
		# testa *m
		lw $t0, 0($a0) # $t0 = *m
		div $t0, $a2
		mfhi $t1 # resto
		
		bnez $t1, testa_n # se resto != 0, não divide
		
		mflo $t2 # quociente
		sw $t2, 0($a0) # *m = *m / d
		li $v0, 1  # retorna 1 pois dividiu
   	
testa_n:
    	lw $t0, 0($a1) # $t0 = *n
    	div $t0, $a2
    	mfhi $t1 # resto

    	bnez $t1, fim_divide # se resto != 0, termina

    	mflo $t2 # quociente
    	sw $t2, 0($a1) # *n = *n / d
    	li $v0, 1 # dividiu

fim_divide:
    	jr $ra
