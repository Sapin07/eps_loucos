package game.core.entities.enemies;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.core.entities.OwnerType;
import game.core.entities.Projectile;
import lib.GameLib;

// Losango magenta - réplica fiel do "enemy2" do Main.java original
// (spawn: linhas 118-128 e 486-511; movimento/curva: 390-436; disparo: 438-460).
// Nasce reto pelo topo, numa de duas colunas fixas (20% ou 80% da largura).
// Ao cruzar 30% da altura da tela, curva - pra esquerda se nasceu na metade
// esquerda, pra direita se nasceu na direita - até ficar voando puramente na
// horizontal. No instante exato em que termina a curva, dispara um leque de
// 3 projéteis (com uma pequena variação aleatória de ângulo) e continua até
// sair de cena pela lateral.
public class EnemyType2 extends Enemy {

    // Vida propositalmente abaixo do menor dano possível do player - morre
    // sempre num hit só, igual ao EnemyType1.
    private static final int MAX_HEALTH = 1;
    private static final double SIZE = 24; // diâmetro; SIZE/2 = raio usado no desenho

    private static final double SPEED = 0.42;      // px/ms - enemy2_V do original
    private static final double TURN_RATE = 0.003; // rad/ms - enemy2_RV do original
    private static final double TURN_TRIGGER_HEIGHT_RATIO = 0.30;

    private static final double PROJECTILE_SPEED = 300;
    private static final double SHOOT_INTERVAL_MS = 0; // não usa intervalo - ver hasTurned abaixo
    private static final int CONTACT_DAMAGE = 1;
    private static final int SHOOT_DAMAGE = 1;

    // Leque de disparo: reto pra baixo (PI/2) +/- 22.5 graus (PI/8)
    private static final double[] SHOT_ANGLES = {
        Math.PI / 2 + Math.PI / 8,
        Math.PI / 2,
        Math.PI / 2 - Math.PI / 8
    };

    private double angle;
    private double angularVelocity = 0.0;
    private boolean hasTurned = false; // true no frame em que a curva termina; consumido em tryShoot()

    public EnemyType2(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HEALTH, Color.RED, SHOOT_INTERVAL_MS, CONTACT_DAMAGE, SHOOT_DAMAGE);
        this.angle = (3 * Math.PI) / 2; // reto pra baixo, igual ao spawn original
    }

    @Override
    public void update(long delta) {
        if (updateExploding(delta)) return;

        // Movimento em coordenadas polares - não usa vx/vy da Entity, então
        // não chamamos super.update(delta) aqui, igual ao Player.
        double previousY = y;

        x += SPEED * Math.cos(angle) * delta;
        y += SPEED * Math.sin(angle) * delta * (-1.0);
        angle += angularVelocity * delta;

        double threshold = GameLib.HEIGHT * TURN_TRIGGER_HEIGHT_RATIO;

        if (previousY < threshold && y >= threshold) {
            angularVelocity = (x < GameLib.WIDTH / 2.0) ? TURN_RATE : -TURN_RATE;
        }

        if (angularVelocity > 0 && Math.abs(angle - 3 * Math.PI) < 0.05) {
            angularVelocity = 0.0;
            angle = 3 * Math.PI;
            hasTurned = true;
        }
        if (angularVelocity < 0 && Math.abs(angle) < 0.05) {
            angularVelocity = 0.0;
            angle = 0.0;
            hasTurned = true;
        }

        if (x < -10 || x > GameLib.WIDTH + 10) {
            deadactivate();; // saiu pela lateral, igual ao original
        }
    }

    @Override
    public void draw() {
        if (drawExplosion()) return;

        GameLib.setColor(Color.RED);
        GameLib.drawDiamond(x, y, SIZE / 2);
    }

    @Override
    public List<Projectile> tryShoot(long currentTime, double targetX, double targetY) {
        if (!hasTurned) {
            return List.of();
        }
        hasTurned = false; // consome o gatilho - atira uma única vez por curva

        List<Projectile> shots = new ArrayList<>(SHOT_ANGLES.length);
        for (double baseAngle : SHOT_ANGLES) {
            double a = baseAngle + Math.random() * Math.PI / 6 - Math.PI / 12;
            double vx = Math.cos(a) * PROJECTILE_SPEED;
            double vy = Math.sin(a) * PROJECTILE_SPEED;
            shots.add(makeDefaultProjectile(x, y, vx, vy, OwnerType.ENEMY, Color.MAGENTA));
        }
        return shots;
    }
}