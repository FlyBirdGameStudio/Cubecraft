package com.flybirdstudio.cubecraftcontent.block

import com.flybirdstudio.cubecraft.world.IWorld
import com.flybirdstudio.cubecraft.world.block.EnumFacing
import com.flybirdstudio.cubecraft.world.block.material.Block
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter
import com.flybirdstudio.util.math.AABB

class BlockBehaviorRegistery {
    @NameSpaceItemGetter(id = "block", namespace = "cubecraft")
    fun block(): Block {
        return object : Block() {
            override fun getEnabledFacings(): Array<EnumFacing> {
                return EnumFacing.all()
            }

            override fun getCollisionBoxSizes(): Array<AABB> {
                return arrayOf(AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
            }

            override fun getSelectionBoxSizes(): Array<AABB> {
                return arrayOf(AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
            }

            override fun getResistance(): Float {
                return 1f
            }

            override fun getDensity(): Float {
                return 10f
            }

            override fun getHardNess(): Int {
                return 4
            }

            override fun getBrakeLevel(): Int {
                return 2
            }

            override fun getTags(): Array<String?> {
                return arrayOfNulls(0)
            }

            override fun isSolid(): Boolean {
                return true
            }

            override fun opacity(): Int {
                return 0 //value is solid ,ignored it
            }

            override fun isBlockEntity(): Boolean {
                return false
            }
        }
    }

    @NameSpaceItemGetter(id = "stone", namespace = "cubecraft")
    fun stone(): Block {
        return object : Block() {
            override fun getEnabledFacings(): Array<EnumFacing> {
                return EnumFacing.all()
            }

            override fun getCollisionBoxSizes(): Array<AABB> {
                return arrayOf(AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
            }

            override fun getSelectionBoxSizes(): Array<AABB> {
                return arrayOf(AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
            }

            override fun getResistance(): Float {
                return 1f
            }

            override fun getDensity(): Float {
                return 10f
            }

            override fun getHardNess(): Int {
                return 4
            }

            override fun getBrakeLevel(): Int {
                return 2
            }

            override fun getTags(): Array<String?> {
                return arrayOfNulls(0)
            }

            override fun isSolid(): Boolean {
                return true
            }

            override fun opacity(): Int {
                return 0 //value is solid ,ignored it
            }

            override fun isBlockEntity(): Boolean {
                return false
            }
        }
    }

    @NameSpaceItemGetter(id = "block_leaves", namespace = "cubecraft")
    fun leaves(): Block {
        return object : Block() {
            override fun getEnabledFacings(): Array<EnumFacing> {
                return EnumFacing.all()
            }

            override fun getCollisionBoxSizes(): Array<AABB> {
                return arrayOf(AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
            }

            override fun getSelectionBoxSizes(): Array<AABB> {
                return arrayOf(AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
            }

            override fun getResistance(): Float {
                return 1f
            }

            override fun getDensity(): Float {
                return 2f
            }

            override fun getHardNess(): Int {
                return 2
            }

            override fun getBrakeLevel(): Int {
                return 0
            }

            override fun getTags(): Array<String?> {
                return arrayOfNulls(0)
            }

            override fun isSolid(): Boolean {
                return false
            }

            override fun opacity(): Int {
                return 0 //value is solid ,ignored it
            }

            override fun isBlockEntity(): Boolean {
                return false
            }
        }
    }

    @NameSpaceItemGetter(id = "air", namespace = "cubecraft")
    fun untouchableBlock(): Block {
        return object : Block() {
            override fun getEnabledFacings(): Array<EnumFacing> {
                return EnumFacing.all()
            }

            override fun getCollisionBoxSizes(): Array<AABB?> {
                return arrayOfNulls(0)
            }

            override fun getSelectionBoxSizes(): Array<AABB?> {
                return arrayOfNulls(0)
            }

            override fun getResistance(): Float {
                return 0f
            }

            override fun getDensity(): Float {
                return 10f
            }

            override fun getHardNess(): Int {
                return 0
            }

            override fun getBrakeLevel(): Int {
                return 0
            }

            override fun getTags(): Array<String?> {
                return arrayOfNulls(0)
            }

            override fun isSolid(): Boolean {
                return false
            }

            override fun opacity(): Int {
                return 0
            }

            override fun isBlockEntity(): Boolean {
                return false
            }

            override fun shouldRender(world: IWorld, x: Long, y: Long, z: Long): Boolean {
                return false
            }
        }
    }

    @NameSpaceItemGetter(id = "dirt", namespace = "cubecraft")
    fun dirt(): Block {
        return object : Block() {
            override fun getEnabledFacings(): Array<EnumFacing> {
                return arrayOf(EnumFacing.Up)
            }

            override fun getCollisionBoxSizes(): Array<AABB> {
                return arrayOf(AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
            }

            override fun getSelectionBoxSizes(): Array<AABB> {
                return arrayOf(AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
            }

            override fun getResistance(): Float {
                return 1f
            }

            override fun getDensity(): Float {
                return 0.48f
            }

            override fun getHardNess(): Int {
                return 4
            }

            override fun getBrakeLevel(): Int {
                return 0
            }

            override fun getTags(): Array<String> {
                return arrayOf(
                        "cubecraft:farm_convertible",
                        "cubecraft:path_convertible",
                        "cubecraft:plantable"
                )
            }

            override fun isSolid(): Boolean {
                return true
            }

            override fun opacity(): Int {
                return 0
            }

            override fun isBlockEntity(): Boolean {
                return false
            }
        }
    }
}