import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class EventManager implements Listener {
	@EventHandler
	public void doRightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getItem() != null) {
			if (e.getItem().getType() == Material.LEASH) {
				Horse[] horses = new Horse[5];
				for (int i = 0; i < 5; i++) {
					double yaw = ((p.getLocation().getYaw() + 90) * Math.PI) / 180;
					double x = Math.cos(yaw);
					double z = Math.sin(yaw);
					Vector direction = new Vector(x, 0, z).normalize();
					Location l = p.getLocation().add(direction.clone().multiply(2.5));
					Horse h = (Horse) p.getWorld().spawnEntity(l, EntityType.HORSE);
					h.setAdult();
					h.setVariant(Variant.HORSE);
					h.setStyle(Style.WHITE);
					h.setVelocity(direction.clone().multiply(7.7 * 0.05).setY(h.getVelocity().getY()));
					h.setAI(false);
					h.setColor(Color.WHITE);
					h.setInvulnerable(true);
					h.setCollidable(false);
					horses[i] = h;
					HorseAbility ha = new HorseAbility(p, horses, direction);
					HorsePlugin.ha.add(ha);
				}
				e.setCancelled(true);
			}
		}
	}
}
