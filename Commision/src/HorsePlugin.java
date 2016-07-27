import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HorsePlugin extends JavaPlugin {
	public static ArrayList<HorseAbility> ha = new ArrayList<HorseAbility>();

	@Override
	public void onEnable() {
		this.getLogger().info("Horses");
		this.getServer().getPluginManager().registerEvents(new EventManager(), this);
		loopMovement();
	}

	private void loopMovement() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (int i = 0; i < ha.size(); i++) {
					HorseAbility ability = ha.get(i);
					Horse[] horses = ability.getHorses();
					if (ability.getDuration() > 0) {
						for (int j = 0; j < horses.length; j++) {
							Horse h = horses[j];
							if (h.isDead() == false) {
								// 7.7 blocks is sprint speed
								h.setVelocity(ability.getDirection().clone().multiply(7.7 * 0.05)
										.setY(h.getVelocity().getY()));
								Location l1 = h.getLocation().add(ability.getDirection().clone().multiply(0.8));
								Location l2 = l1.clone().add(0, 1.0, 0);
								if (l1.getBlock().isEmpty() == false && l1.getBlock().getType().isSolid()) {
									if (l2.getBlock().isEmpty() == false && l2.getBlock().getType().isSolid()) {
										h.remove();
									}
								}
								// Horse is 0.8 blocks wide/long and player is
								// 0.3 wide/long
								for (Player p : h.getWorld().getPlayers()) {
									if (p.getUniqueId().compareTo(ability.getUser().getUniqueId()) != 0) {
										double dx = Math.abs(p.getLocation().getX() - h.getLocation().getX());
										double dz = Math.abs(p.getLocation().getZ() - h.getLocation().getZ());
										if (dx < 1.1 && dz < 1.1) {
											double dy = p.getLocation().getY() - h.getLocation().getY();
											if (dy < 1.3964844 && dy > -1.8) {
												p.damage(4.0);
												p.setVelocity(p.getVelocity()
														.add(p.getLocation().toVector()
																.subtract(h.getLocation().toVector()).normalize()
																.setY(0.1).multiply(0.05)));
											}
										}
									}
								}
							}
						}
						ability.decreaseDuration();
					} else {
						for (int j = 0; j < horses.length; j++) {
							Horse h = horses[j];
							h.remove();
						}
						ha.remove(i);
					}
				}
			}
		}, 0, 1);

	}

	@Override
	public void onDisable() {
		this.getLogger().info("Removing Horses");
	}
}
