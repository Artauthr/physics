package com.art.prototype.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lombok.Data;

public class CollisionChecker {

    public CollisionChecker () {

    }

    private static Vector2 tmp = new Vector2();

    private static RayVsRectResult tempResult = new RayVsRectResult();

    public static RayVsRectResult rayIntersectsRect(Vector2 rayOrigin, Vector2 rayDirection, Rectangle target) {
        if (rayDirection.x == 0 && rayDirection.y == 0) {
            return null;
        }

        float nearX = (target.x - rayOrigin.x) / rayDirection.x;
        float farX = (target.x + target.width - rayOrigin.x) / rayDirection.x;

        float nearY = (target.y + target.height - rayOrigin.y) / rayDirection.y;
        float farY = (target.y - rayOrigin.y) / rayDirection.y;

        if (nearX > farX) {
            // Swap x
            float swap = nearX;
            nearX = farX;
            farX = swap;

        }

        if (nearY > farY) {
            // Swap y
            float swap = nearY;
            nearY = farY;
            farY = swap;
        }

        if (nearX > farY || nearY > farX) return null;

        tempResult.nearHitTime = Math.max(nearX, nearY);
        float farHitT = Math.min(farX, farY);

        if (farHitT < 0) return null;

        tempResult.contactPoint = rayOrigin.cpy();

        tmp.set(rayDirection);
        tmp.scl(tempResult.nearHitTime);
        tempResult.contactPoint.add(tmp);

        // Set contact normal
        if (nearX > nearY) {
            tempResult.contactNormal.set(rayDirection.x < 0 ? 1 : -1, 0);
        } else if (nearY > nearX) {
            tempResult.contactNormal.set(0, rayDirection.y < 0 ? 1 : -1);
        } else {
            tempResult.contactNormal.setZero();
        }

        return tempResult;
    }

     @Data
     public static class RayVsRectResult implements Pool.Poolable {
        private Vector2 contactPoint = new Vector2();
        private Vector2 contactNormal = new Vector2();
        private float nearHitTime = 0;

        public RayVsRectResult () {
        }

        @Override
        public void reset() {
            contactPoint.setZero();
            contactNormal.setZero();
            nearHitTime = 0;
        }
    }


}
