import request from '@/utils/request'

// 创建雷达模块banner图
export function createBanner(data) {
  return request({
    url: '/radar/banner/create',
    method: 'post',
    data: data
  })
}

// 更新雷达模块banner图
export function updateBanner(data) {
  return request({
    url: '/radar/banner/update',
    method: 'put',
    data: data
  })
}

// 删除雷达模块banner图
export function deleteBanner(id) {
  return request({
    url: '/radar/banner/delete?id=' + id,
    method: 'delete'
  })
}

// 获得雷达模块banner图
export function getBanner(id) {
  return request({
    url: '/radar/banner/get?id=' + id,
    method: 'get'
  })
}

// 获得雷达模块banner图分页
export function getBannerPage(query) {
  return request({
    url: '/radar/banner/page',
    method: 'get',
    params: query
  })
}

// 导出雷达模块banner图 Excel
export function exportBannerExcel(query) {
  return request({
    url: '/radar/banner/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
