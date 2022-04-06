import request from '@/utils/request'

/**
 * 获取redis 监控信息
 * @returns Promise<any>
 */
export function redisMonitorInfo() {
    return request({
        url: '/infra/redis/get-monitor-info',
        method: 'get',
    })
}

/**
 * 获取redis key列表
 * @returns Promise<any>
 */
export function redisKeysInfo() {
    return request({
        url: '/infra/redis/get-key-list',
        method: 'get',
    })
}