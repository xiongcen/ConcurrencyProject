package com.xiongcen.test;

/**
 * Created by xiongcen on 16/11/15.
 */
public class EnumTest {

    enum NetworkPolicy {

        /**
         * Skips checking the disk cache and forces loading through the network.
         */
        // 001
        NO_CACHE(1 << 0),

        /**
         * Skips storing the result into the disk cache.
         * <p/>
         * <em>Note</em>: At this time this is only supported if you are using OkHttp.
         */
        // 010
        NO_STORE(1 << 1),

        /**
         * Forces the request through the disk cache only, skipping network.
         */
        // 100
        OFFLINE(1 << 2);

        public static boolean shouldReadFromDiskCache(int networkPolicy) {
            return (networkPolicy & NetworkPolicy.NO_CACHE.index) == 0;
        }

        public static boolean shouldWriteToDiskCache(int networkPolicy) {
            return (networkPolicy & NetworkPolicy.NO_STORE.index) == 0;
        }

        public static boolean isOfflineOnly(int networkPolicy) {
            return (networkPolicy & NetworkPolicy.OFFLINE.index) != 0;
        }

        final int index;

        private NetworkPolicy(int index) {
            this.index = index;
        }
    }

    public static void main(String[] args) {
        System.out.println("NetworkPolicy.NO_CACHE.index=" + NetworkPolicy.NO_CACHE.index);
        System.out.println("NetworkPolicy.NO_STORE.index=" + NetworkPolicy.NO_STORE.index);
        System.out.println("NetworkPolicy.OFFLINE.index=" + NetworkPolicy.OFFLINE.index);

        System.out.println("NetworkPolicy.NO_CACHE.ordinal()=" + NetworkPolicy.NO_CACHE.ordinal());
        System.out.println("NetworkPolicy.NO_STORE.ordinal()=" + NetworkPolicy.NO_STORE.ordinal());
        System.out.println("NetworkPolicy.OFFLINE.ordinal()=" + NetworkPolicy.OFFLINE.ordinal());
    }
}/**
 * NetworkPolicy.NO_CACHE.index=1
 * NetworkPolicy.NO_STORE.index=2
 * NetworkPolicy.OFFLINE.index=4
 * NetworkPolicy.NO_CACHE.ordinal()=0
 * NetworkPolicy.NO_STORE.ordinal()=1
 * NetworkPolicy.OFFLINE.ordinal()=2
 */
