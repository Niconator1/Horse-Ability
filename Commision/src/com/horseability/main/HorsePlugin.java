package com.horseability.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.darkblade12.particleeffect.ParticleEffect;

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
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getExp() > 0) {
						p.setExp(p.getExp() - 1.0f / (60.0f * 20.0f));
						if (p.getExp() > 0) {
							p.setLevel((int) Math.round(p.getExp() / 1.0 * 60.0));
						} else {
							p.setLevel(0);
						}
					}
				}
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
								Vector vec = HorseUtil.rotateAroundAxisY(ability.getDirection().clone().multiply(0.8),
										0.5 * Math.PI);
								Location start = h.getLocation().add(vec);
								Location l1 = start.add(ability.getDirection().clone().multiply(0.9));
								Location l2 = l1.clone().add(0, 1.0, 0);
								if (l1.getBlock().isEmpty() == false && l1.getBlock().getType().isSolid()) {
									if (l2.getBlock().isEmpty() == false && l2.getBlock().getType().isSolid()) {
										ParticleEffect.SMOKE_LARGE.display(0.3f, 0.6f, 0.3f, 0.1f, 50,
												h.getLocation().add(0, 0.2, 0), 15);
										h.remove();
									}
								}
								Location l3 = l1.add(vec.multiply(-1));
								Location l4 = l2.add(vec);
								if (l3.getBlock().isEmpty() == false && l3.getBlock().getType().isSolid()) {
									if (l4.getBlock().isEmpty() == false && l4.getBlock().getType().isSolid()) {
										ParticleEffect.SMOKE_LARGE.display(0.3f, 0.6f, 0.3f, 0.1f, 50,
												h.getLocation().add(0, 0.2, 0), 15);
										h.remove();
									}
								}
								Location l5 = l3.add(vec);
								Location l6 = l4.add(vec);
								if (l5.getBlock().isEmpty() == false && l5.getBlock().getType().isSolid()) {
									if (l6.getBlock().isEmpty() == false && l6.getBlock().getType().isSolid()) {
										ParticleEffect.SMOKE_LARGE.display(0.3f, 0.6f, 0.3f, 0.1f, 50,
												h.getLocation().add(0, 0.2, 0), 15);
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
												if(p.getGameMode()==GameMode.ADVENTURE||p.getGameMode()==GameMode.SURVIVAL){
													p.damage(4.0);
													p.setVelocity(p.getVelocity()
															.add(p.getLocation().toVector()
																	.subtract(h.getLocation().toVector()).normalize()
																	.setY(0.2).multiply(0.05)));
												}
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
							ParticleEffect.SMOKE_LARGE.display(0.3f, 0.6f, 0.3f, 0.1f, 50,
									h.getLocation().add(0, 0.2, 0), 15);
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
		for (int i = 0; i < ha.size(); i++) {
			HorseAbility ability = ha.get(i);
			Horse[] horses = ability.getHorses();
			for (int j = 0; j < horses.length; j++) {
				Horse h = horses[j];
				h.remove();
			}
		}
	}
}
