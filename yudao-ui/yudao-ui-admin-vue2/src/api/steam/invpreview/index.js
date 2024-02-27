import request from '@/utils/request'

// 创建饰品在售预览
export function createInvPreview(data) {
  return request({
    url: '/steam/inv-preview/create',
    method: 'post',
    data: data
  })
}

// 更新饰品在售预览
export function updateInvPreview(data) {
  return request({
    url: '/steam/inv-preview/update',
    method: 'put',
    data: data
  })
}

// 删除饰品在售预览
export function deleteInvPreview(id) {
  return request({
    url: '/steam/inv-preview/delete?id=' + id,
    method: 'delete'
  })
}

// 获得饰品在售预览
export function getInvPreview(id) {
  return request({
    url: '/steam/inv-preview/get?id=' + id,
    method: 'get'
  })
}

// 获得饰品在售预览分页
export function getInvPreviewPage(params) {
  return request({
    url: '/steam/inv-preview/page',
    method: 'get',
    params
  })
}
// 导出饰品在售预览 Excel
export function exportInvPreviewExcel(params) {
  return request({
    url: '/steam/inv-preview/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}