package net.oskarstrom.dashloader.def.data.serialize.mapping;

import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.oskarstrom.dashloader.api.Dashable;
import net.oskarstrom.dashloader.api.data.Pointer2ObjectMap;
import net.oskarstrom.dashloader.api.registry.DashRegistry;
import net.oskarstrom.dashloader.def.DashLoader;
import net.oskarstrom.dashloader.def.data.VanillaData;

public class DashBlockStateData implements Dashable<Object2IntMap<BlockState>> {

	@Serialize(order = 0)
	public final Pointer2ObjectMap<Integer> blockstates;

	public DashBlockStateData(@Deserialize("blockstates") Pointer2ObjectMap<Integer> blockstates) {
		this.blockstates = blockstates;
	}

	public DashBlockStateData(VanillaData data, DashRegistry registry, final DashLoader.TaskHandler taskHandler) {
		this.blockstates = new Pointer2ObjectMap<>();
		final Object2IntMap<BlockState> stateLookup = data.getStateLookup();
		taskHandler.setSubtasks(stateLookup.size());
		stateLookup.forEach((blockState, integer) -> {
			this.blockstates.add(Pointer2ObjectMap.Entry.of(registry.add(blockState), integer));
			taskHandler.completedSubTask();
		});
	}

	public Object2IntMap<BlockState> toUndash(DashRegistry registry) {
		final Object2IntOpenHashMap<BlockState> stateLookupOut = new Object2IntOpenHashMap<>();
		blockstates.forEach((entry) -> stateLookupOut.put(registry.get(entry.key), entry.value));
		return stateLookupOut;
	}

}
