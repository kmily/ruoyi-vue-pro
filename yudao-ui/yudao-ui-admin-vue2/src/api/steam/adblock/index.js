import request from '@/utils/request'

// 创建广告位
export function createAdBlock(data) {
  return request({
    url: '/steam/ad-block/create',
    method: 'post',
    data: data
  })
}

// 更新广告位
export function updateAdBlock(data) {
  return request({
    url: '/steam/ad-block/update',
    method: 'put',
    data: data
  })
}

// 删除广告位
export function deleteAdBlock(id) {
  return request({
    url: '/steam/ad-block/delete?id=' + id,
    method: 'delete'
  })
}

// 获得广告位
export function getAdBlock(id) {
  return request({
    url: '/steam/ad-block/get?id=' + id,
    method: 'get'
  })
}

// 获得广告位分页
export function getAdBlockPage(params) {
  return request({
    url: '/steam/ad-block/page',
    method: 'get',
    params
  })
}
// 导出广告位 Excel
export function exportAdBlockExcel(params) {
  return request({
    url: '/steam/ad-block/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 子表（广告） ====================
      // 获得广告列表
    export function getAdListByBlockId(blockId) {
      return request({
        url: '/steam/ad-block/ad/list-by-block-id?blockId=' + blockId,
        method: 'get'
      })
    }
  