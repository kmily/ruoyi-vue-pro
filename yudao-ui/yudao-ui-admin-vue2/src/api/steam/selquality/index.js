import request from '@/utils/request'

// 创建类别选择
export function createSelQuality(data) {
  return request({
    url: '/steam/sel-quality/create',
    method: 'post',
    data: data
  })
}

// 更新类别选择
export function updateSelQuality(data) {
  return request({
    url: '/steam/sel-quality/update',
    method: 'put',
    data: data
  })
}

// 删除类别选择
export function deleteSelQuality(id) {
  return request({
    url: '/steam/sel-quality/delete?id=' + id,
    method: 'delete'
  })
}

// 获得类别选择
export function getSelQuality(id) {
  return request({
    url: '/steam/sel-quality/get?id=' + id,
    method: 'get'
  })
}

// 获得类别选择分页
export function getSelQualityPage(params) {
  return request({
    url: '/steam/sel-quality/page',
    method: 'get',
    params
  })
}
// 导出类别选择 Excel
export function exportSelQualityExcel(params) {
  return request({
    url: '/steam/sel-quality/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
