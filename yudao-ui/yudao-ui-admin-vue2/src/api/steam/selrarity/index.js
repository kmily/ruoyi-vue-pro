import request from '@/utils/request'

// 创建品质选择
export function createSelRarity(data) {
  return request({
    url: '/steam/sel-rarity/create',
    method: 'post',
    data: data
  })
}

// 更新品质选择
export function updateSelRarity(data) {
  return request({
    url: '/steam/sel-rarity/update',
    method: 'put',
    data: data
  })
}

// 删除品质选择
export function deleteSelRarity(id) {
  return request({
    url: '/steam/sel-rarity/delete?id=' + id,
    method: 'delete'
  })
}

// 获得品质选择
export function getSelRarity(id) {
  return request({
    url: '/steam/sel-rarity/get?id=' + id,
    method: 'get'
  })
}

// 获得品质选择分页
export function getSelRarityPage(params) {
  return request({
    url: '/steam/sel-rarity/page',
    method: 'get',
    params
  })
}
// 导出品质选择 Excel
export function exportSelRarityExcel(params) {
  return request({
    url: '/steam/sel-rarity/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}