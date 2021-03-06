package cls.equipment;

public class Equipment {
	
	public enum Slot {
		HEAD,  // helmets, hoods, etc.
		NECK,  // amulets, necklaces, etc.
		CHEST, // robes, armour, etc.
		LEGS,  // trousers 
		HANDS, // gloves, claws, etc.
	}
	
	public final String name;
	public final Slot slot;

	public Equipment(String name, Slot slot) {
		this.name = name;
		this.slot = slot;
	}

}
